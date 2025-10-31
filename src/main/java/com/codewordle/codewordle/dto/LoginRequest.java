package com.codewordle.codewordle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing the data required for a user to log in.
 * It includes validation constraints to ensure the presence of required fields.
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * The email of the user trying to log in. In our system, this corresponds
     * to the 'email' field of the User entity.
     * It cannot be blank.
     */
    @NotBlank(message = "Email cannot be blank")
    private String email;

    /**
     * The plain-text password of the user.
     * It cannot be blank.
     */
    @NotBlank(message = "Password cannot be blank")
    private String password;
}