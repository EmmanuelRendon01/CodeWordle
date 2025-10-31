package com.codewordle.codewordle.config;

import com.codewordle.codewordle.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that intercepts incoming HTTP requests to validate JWTs.
 * This filter runs once per request and is the primary mechanism for
 * stateless, token-based authentication.
 */
@RequiredArgsConstructor // Creates a constructor with all final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Our CustomUserDetailsService

    /**
     * The main logic of the filter.
     * It checks for a JWT in the Authorization header, validates it, and sets
     * the authentication in Spring's SecurityContext.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Check if the JWT is present in the header and is well-formed.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Pass to the next filter if no token.
            return;
        }

        // 2. Extract the token from the "Bearer " prefix.
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        // 3. Validate the token.
        // If username is present and the user is not already authenticated.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // If the token is valid for this user.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication object.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials are not needed for token-based auth.
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Update the SecurityContextHolder: The user is now authenticated for this request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 4. Continue the filter chain.
        filterChain.doFilter(request, response);
    }
}