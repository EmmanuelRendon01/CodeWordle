package com.codewordle.codewordle.config;

import com.codewordle.codewordle.service.CustomUserDetailsService;
import com.codewordle.codewordle.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration for the application.
 * This version uses the modern, non-deprecated approach for Spring Security 6+.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    // We no longer need to inject the UserDetailsService here directly.
    // Spring will find our @Service-annotated CustomUserDetailsService bean automatically.


    /**
     * SecurityFilterChain with HIGHEST precedence (Order 0)
     * for the H2 database console.
     * We use PathRequest.toH2Console() to correctly override Spring Boot's
     * default security for the H2 console.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // ESTA ES LA LÍNEA MÁGICA Y CORRECTA
                .securityMatcher(PathRequest.toH2Console())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        return http.build();
    }

    /**
     * La ÚNICA SecurityFilterChain principal para TODA la aplicación (Order 1).
     * Implementa la seguridad de API stateless con JWT.
     * Se ha ELIMINADO la antigua 'webSecurityFilterChain'.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                // Esta cadena ahora es el "catch-all" para todo lo que no sea H2.
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                // Endpoints de la API de autenticación son públicos
                                .requestMatchers("/auth/**").permitAll()
                                // PÁGINAS (JSPs) que cualquiera puede ver
                                .requestMatchers("/login", "/register", "/dashboard").permitAll()
                                // RECURSOS ESTÁTICOS (CSS, JS) son públicos
                                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                                // RUTA DE ARCHIVOS JSP es pública (para el forward interno)
                                .requestMatchers("/WEB-INF/jsp/**").permitAll()
                                // TODO LO DEMÁS (ej: /api/profile, /api/game) requiere autenticación
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Exposes the AuthenticationManager from the configuration as a Bean.
     * This is required by our AuthService to trigger the authentication process.
     * It automatically uses the configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides the PasswordEncoder bean for hashing passwords.
     * Spring Security will discover this bean and use it.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * The AuthenticationProvider bean is no longer explicitly needed.
     * Spring Security will automatically create a DaoAuthenticationProvider
     * and configure it with our custom UserDetailsService and PasswordEncoder beans
     * when the AuthenticationManager is requested.
     */
}