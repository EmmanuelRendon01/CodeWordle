package com.codewordle.codewordle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object representing the initial state of a newly created game.
 * This is the response sent to the frontend after a game is successfully started.
 */
@Getter
@AllArgsConstructor
public class GameStateResponse {

    /**
     * The unique identifier for the newly created game.
     * The frontend must store this ID to make subsequent guess requests.
     */
    private Long gameId;

    /**
     * The length of the target word.
     * The frontend uses this to dynamically build the game board grid.
     */
    private int wordLength;

    /**
     * The maximum number of attempts allowed for the game.
     * The frontend uses this to build the correct number of rows on the board.
     */
    private int maxAttempts;
}