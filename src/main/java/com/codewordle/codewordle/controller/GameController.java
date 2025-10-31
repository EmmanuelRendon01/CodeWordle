package com.codewordle.codewordle.controller;

import com.codewordle.codewordle.dto.*;
import com.codewordle.codewordle.model.Game;
import com.codewordle.codewordle.model.User;
import com.codewordle.codewordle.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling all game-related actions.
 * All endpoints within this controller are protected by Spring Security and require
 * a valid JWT for authentication.
 */
@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private static final int MAX_ATTEMPTS = 6;

    /**
     * Endpoint for an authenticated user to start a new game.
     * The user's identity is retrieved directly from the security context.
     *
     * @param currentUser The authenticated User object, injected by Spring Security.
     * @param request The request body containing the topic for the new game.
     * @return A ResponseEntity containing the initial state of the newly created game,
     *         including the game ID, word length, and max attempts.
     */
    @PostMapping("/start")
    public ResponseEntity<GameStateResponse> startGame(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody StartGameRequest request) {

        Game newGame = gameService.startNewGame(currentUser, request.getTopic());

        GameStateResponse response = new GameStateResponse(
                newGame.getId(),
                newGame.getTargetWord().getText().length(),
                MAX_ATTEMPTS
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for an authenticated user to submit a guess for an ongoing game.
     *
     * @param currentUser The authenticated User object.
     * @param gameId The ID of the game, extracted from the URL path.
     * @param request The request body containing the user's guessed word.
     * @return A ResponseEntity containing detailed feedback for the guess, the remaining
     *         attempts, and the current status of the game (IN_PROGRESS, WON, or LOST).
     */
    @PostMapping("/{gameId}/guess")
    public ResponseEntity<GuessResult> makeGuess(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long gameId,
            @Valid @RequestBody GuessRequest request) {

        GuessResult result = gameService.makeGuess(gameId, request.getWord(), currentUser);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint to check for and retrieve the state of an active game for the current user.
     *
     * @param currentUser The authenticated user.
     * @return A 200 OK response with the game state if a game is in progress,
     *         or a 204 No Content response if no active game is found.
     */
    @GetMapping("/active")
    public ResponseEntity<ActiveGameStateResponse> getActiveGame(@AuthenticationPrincipal User currentUser) {
        return gameService.getActiveGameForUser(currentUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}