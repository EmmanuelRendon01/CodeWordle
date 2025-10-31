package com.codewordle.codewordle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for the request to start a new game.
 * It carries the topic selected by the user from the frontend to the backend.
 */
@Getter
@Setter
public class StartGameRequest {

    /**
     * The topic for the new game (e.g., "Java", "Spring").
     * This field is mandatory.
     */
    @NotBlank(message = "Topic cannot be blank")
    private String topic;
}