package com.codewordle.codewordle.service;

import com.codewordle.codewordle.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the UserDetailsService interface.
 * This service is responsible for loading a user's details from the database
 * given their username. Spring Security will use this service during the
 * authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs the service with the required UserRepository.
     * @param userRepository The repository for accessing user data.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username. In our application, the
     * 'email' corresponds to the 'email' field in our User entity.
     * <p>
     * This method is called by the DaoAuthenticationProvider during the authentication process.
     *
     * @param email the email (in our case, the 'email') identifying the user whose data is required.
     * @return a fully populated UserDetails object (our User entity).
     * @throws UsernameNotFoundException if the user could not be found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // We use the UserRepository to find the user by their unique email.
        // If the user is not found, we must throw UsernameNotFoundException as per the contract.
        // Spring Security catches this exception and understands it as a failed authentication attempt.
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}