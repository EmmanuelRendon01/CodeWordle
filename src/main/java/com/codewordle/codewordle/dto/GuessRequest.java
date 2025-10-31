package com.codewordle.codewordle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for the request to make a guess in a game.
 * It carries the word submitted by the user.
 */
@Getter
@Setter
public class GuessRequest {

    /**
     * The word guessed by the user.
     * This field is mandatory.
     */
    @NotBlank(message = "Guessed word cannot be blank")
    private String word;
}