package com.codewordle.codewordle.controller;


import com.codewordle.codewordle.dto.LoginRequest;
import com.codewordle.codewordle.dto.LoginResponse;
import com.codewordle.codewordle.dto.UserCreateRequest;
import com.codewordle.codewordle.dto.UserResponse;
import com.codewordle.codewordle.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ANTES (con @RequestBody para JSON):
     * public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request)
     *
     * AHORA (con @ModelAttribute para datos de formulario):
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request){
        UserResponse createdUserResponse = authService.Register(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{id}")
                .buildAndExpand(createdUserResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUserResponse);
    }

    /**
     * Handles user authentication requests.
     * Takes login credentials and, upon successful validation, returns a JWT.
     *
     * @param request The LoginRequest DTO containing the user's credentials.
     * @return a ResponseEntity containing a LoginResponse with the JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = this.authService.Login(request);
        return ResponseEntity.ok(response);
    }
}
