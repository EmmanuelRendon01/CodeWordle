package com.codewordle.codewordle.dto;

import com.codewordle.codewordle.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * DTO to represent the full state of an active game,
 * used to resume a game in progress.
 */
@Getter
@AllArgsConstructor
public class ActiveGameStateResponse {
    private Long gameId;
    private int wordLength;
    private int maxAttempts;
    private List<List<LetterFeedback>> previousGuesses; // Una lista de intentos, donde cada intento es una lista de letras
}