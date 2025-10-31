package com.codewordle.codewordle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO representing the successful response after a user logs in.
 * It contains the generated JSON Web Token (JWT).
 */
@Getter
@AllArgsConstructor // Generates a constructor with all fields
public class LoginResponse {

    /**
     * The JWT that the client should use for authenticating subsequent requests.
     */
    private final String token;

}