package com.codewordle.codewordle.service.impl;

import com.codewordle.codewordle.dto.LoginRequest;
import com.codewordle.codewordle.dto.LoginResponse;
import com.codewordle.codewordle.dto.UserCreateRequest;
import com.codewordle.codewordle.dto.UserResponse;
import com.codewordle.codewordle.mapper.UserMapper;
import com.codewordle.codewordle.model.User;
import com.codewordle.codewordle.repository.UserRepository;
import com.codewordle.codewordle.service.AuthService;
import com.codewordle.codewordle.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponse Register(UserCreateRequest newUser) {

        User user = userMapper.toEntity(newUser);

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(hashedPassword);

        user.setRole("ROLE_USER");

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    /**
     * Authenticates a user based on the provided credentials and generates a JWT.
     *
     * @param request The login request containing username and password.
     * @return a LoginResponse DTO containing the generated JWT.
     * @throws org.springframework.security.core.AuthenticationException if credentials are invalid.
     */
    public LoginResponse Login(LoginRequest request) {
        // Step 1: Trigger the authentication process using Spring Security's AuthenticationManager.
        // This manager will use our CustomUserDetailsService to find the user
        // and our PasswordEncoder to validate the password. If credentials are
        // incorrect, it will throw an AuthenticationException.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Step 2: If authentication was successful, find the user.
        // We need the UserDetails object to generate the token.
        var user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Should not fail if authentication passed.

        // Step 3: Generate the JWT using our JwtService.
        String token = this.jwtService.generateToken(user);

        // Step 4: Return the response containing the token.
        return new LoginResponse(token);
    }
}
