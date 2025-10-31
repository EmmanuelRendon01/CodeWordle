package com.codewordle.codewordle.service;

import com.codewordle.codewordle.dto.ActiveGameStateResponse;
import com.codewordle.codewordle.dto.GuessResult;
import com.codewordle.codewordle.dto.LetterFeedback;
import com.codewordle.codewordle.model.*;
import com.codewordle.codewordle.repository.GameRepository;
import com.codewordle.codewordle.repository.GuessRepository;
import com.codewordle.codewordle.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service class containing the core business logic for the CodeWordle game.
 * It handles starting games, processing guesses, and determining game outcomes.
 */
@Service
public class GameService {

    private static final int MAX_ATTEMPTS = 6;

    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    private final GuessRepository guessRepository;

    public GameService(GameRepository gameRepository, WordRepository wordRepository, GuessRepository guessRepository) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
        this.guessRepository = guessRepository;
    }

    /**
     * Starts a new game for a given user and topic.
     * It selects a random word from the specified topic and creates a new game record.
     *
     * @param user The user starting the game.
     * @param topic The selected topic for the word.
     * @return The newly created Game object.
     * @throws IllegalStateException if the user already has a game in progress.
     * @throws IllegalArgumentException if no words are found for the topic.
     */
    @Transactional
    public Game startNewGame(User user, String topic) {
        // A business rule: a user cannot have more than one game in progress.
        if (gameRepository.findByUserAndStatus(user, GameStatus.IN_PROGRESS).isPresent()) {
            throw new IllegalStateException("User already has a game in progress.");
        }

        List<Word> words = wordRepository.findByTopic(topic);
        if (words.isEmpty()) {
            throw new IllegalArgumentException("No words found for the topic: " + topic);
        }

        // Select a random word from the list for the chosen topic.
        Word targetWord = words.get(new Random().nextInt(words.size()));

        Game newGame = new Game();
        newGame.setUser(user);
        newGame.setTargetWord(targetWord);
        newGame.setStatus(GameStatus.IN_PROGRESS);
        newGame.setStartTime(LocalDateTime.now());

        return gameRepository.save(newGame);
    }

    /**
     * Processes a user's guess for a specific game, providing feedback and updating the game state.
     *
     * @param gameId The ID of the game being played.
     * @param guessedWord The word guessed by the user.
     * @param user The user making the guess.
     * @return A GuessResult DTO containing feedback for each letter and the current game state.
     * @throws SecurityException if the user is not the owner of the game.
     * @throws IllegalStateException if the game is not in progress.
     * @throws IllegalArgumentException if the game is not found or the guess has an incorrect length.
     */
    @Transactional
    public GuessResult makeGuess(Long gameId, String guessedWord, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with id: " + gameId));

        // --- VALIDATIONS (Guard Clauses) ---
        if (game.getUser().getId() != user.getId()) {
            throw new SecurityException("User is not authorized to make a guess in this game.");
        }
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress. Its status is " + game.getStatus());
        }
        if (guessedWord.length() != game.getTargetWord().getText().length()) {
            throw new IllegalArgumentException("Guess has an incorrect length. Expected " + game.getTargetWord().getText().length() + " but got " + guessedWord.length());
        }

        String normalizedGuess = guessedWord.toUpperCase();

        // Persist the user's guess
        Guess guess = new Guess();
        guess.setGame(game);
        guess.setGuessedWord(normalizedGuess);
        guess.setTimestamp(LocalDateTime.now());
        guessRepository.save(guess);

        // --- CORE GAME LOGIC ---
        List<LetterFeedback> feedback = calculateFeedback(normalizedGuess, game.getTargetWord().getText());
        int attempts = guessRepository.countByGame(game);

        // Check for win/loss conditions
        if (normalizedGuess.equals(game.getTargetWord().getText())) {
            game.setStatus(GameStatus.WON);
            game.setEndTime(LocalDateTime.now());
        } else if (attempts >= MAX_ATTEMPTS) {
            game.setStatus(GameStatus.LOST);
            game.setEndTime(LocalDateTime.now());
        }
        gameRepository.save(game); // Save the updated game status

        int remainingAttempts = MAX_ATTEMPTS - attempts;
        return new GuessResult(game.getStatus(), feedback, remainingAttempts, game.getTargetWord().getText());
    }

    /**
     * Helper method to calculate letter-by-letter feedback for a guess, implementing the core Wordle logic.
     * This two-pass algorithm correctly handles duplicate letters.
     *
     * @param guess The user's guessed word (normalized to uppercase).
     * @param target The target word (normalized to uppercase).
     * @return A list of LetterFeedback objects.
     */
    private List<LetterFeedback> calculateFeedback(String guess, String target) {
        List<LetterFeedback> feedbackList = new ArrayList<>();
        char[] guessChars = guess.toCharArray();
        char[] targetChars = target.toCharArray();
        boolean[] targetLetterUsed = new boolean[target.length()]; // Tracks used letters in the target word

        // First pass: check for CORRECT_POSITION (Green) matches.
        // These have priority and "claim" letters from the target word.
        for (int i = 0; i < guess.length(); i++) {
            if (guessChars[i] == targetChars[i]) {
                feedbackList.add(new LetterFeedback(guessChars[i], FeedbackStatus.CORRECT_POSITION));
                targetLetterUsed[i] = true; // Mark this letter as used
            } else {
                feedbackList.add(null); // Add a placeholder for the second pass
            }
        }

        // Second pass: check for WRONG_POSITION (Yellow) and INCORRECT (Gray) matches.
        for (int i = 0; i < guess.length(); i++) {
            if (feedbackList.get(i) == null) { // Only process letters that weren't a perfect match
                boolean foundInWrongPosition = false;
                for (int j = 0; j < target.length(); j++) {
                    // Check if the letter exists in the target AND that specific instance hasn't been used yet
                    if (guessChars[i] == targetChars[j] && !targetLetterUsed[j]) {
                        feedbackList.set(i, new LetterFeedback(guessChars[i], FeedbackStatus.WRONG_POSITION));
                        targetLetterUsed[j] = true; // Mark this letter as used to handle duplicates
                        foundInWrongPosition = true;
                        break; // Move to the next letter in the guess
                    }
                }
                // If after checking all target letters it was not found, it's incorrect.
                if (!foundInWrongPosition) {
                    feedbackList.set(i, new LetterFeedback(guessChars[i], FeedbackStatus.INCORRECT));
                }
            }
        }
        return feedbackList;
    }

    /**
     * Finds and returns the state of an active game for a specific user.
     *
     * @param user The currently authenticated user.
     * @return An Optional containing the full game state if an active game is found.
     */
    @Transactional(readOnly = true) // Es una operación de solo lectura
    public Optional<ActiveGameStateResponse> getActiveGameForUser(User user) {
        Optional<Game> activeGameOpt = gameRepository.findByUserAndStatus(user, GameStatus.IN_PROGRESS);

        if (activeGameOpt.isEmpty()) {
            return Optional.empty();
        }

        Game activeGame = activeGameOpt.get();
        List<Guess> guesses = guessRepository.findByGameOrderByTimestampAsc(activeGame);

        List<List<LetterFeedback>> previousGuessesFeedback = new ArrayList<>();
        for (Guess guess : guesses) {
            previousGuessesFeedback.add(calculateFeedback(guess.getGuessedWord(), activeGame.getTargetWord().getText()));
        }

        ActiveGameStateResponse response = new ActiveGameStateResponse(
                activeGame.getId(),
                activeGame.getTargetWord().getText().length(),
                MAX_ATTEMPTS,
                previousGuessesFeedback
        );

        return Optional.of(response);
    }
}
