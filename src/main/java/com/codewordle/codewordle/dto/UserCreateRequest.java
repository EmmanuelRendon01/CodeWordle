package com.codewordle.codewordle.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a new user creation request.
 * It contains format validations and business logic (password confirmation).
 */
@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters.")
//    @Pattern(regexp = "^[a-zA-Z0-9_\\-\\.]+$", message = "Name can only contain letters, numbers, underscores, hyphens, and dots.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email format is not valid.")
    @Size(max = 100, message = "Email cannot exceed 100 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @NotBlank(message = "Password confirmation is required.")
    private String confirmPassword;

    /**
     * Validates that the password and confirmPassword fields match.
     * This is a fundamental business rule for user registration.
     */
    @AssertTrue(message = "Passwords do not match.")
    public boolean isPasswordConfirmed() {
        // Ensures the logic doesn't trigger if fields are empty,
        // letting @NotBlank handle those cases first.
        if (password == null || confirmPassword == null) {
            return true;
        }
        return password.equals(confirmPassword);
    }
}
