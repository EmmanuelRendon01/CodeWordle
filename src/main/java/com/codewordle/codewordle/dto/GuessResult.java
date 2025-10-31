package com.codewordle.codewordle.dto;
import com.codewordle.codewordle.model.GameStatus;
import lombok.Getter;
import java.util.List;

@Getter
public class GuessResult {
    private final GameStatus gameStatus;
    private final List<LetterFeedback> feedback;
    private final int remainingAttempts;
    private final String correctWord;

    public GuessResult(GameStatus gameStatus, List<LetterFeedback> feedback, int remainingAttempts, String correctWord) {
        this.gameStatus = gameStatus;
        this.feedback = feedback;
        this.remainingAttempts = remainingAttempts;
        this.correctWord = (gameStatus == GameStatus.WON || gameStatus == GameStatus.LOST) ? correctWord : null;
    }
}