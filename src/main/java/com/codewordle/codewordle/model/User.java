package com.codewordle.codewordle.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the system.
 * This entity implements Spring Security's UserDetails interface,
 * allowing it to be used directly by the security framework.
 * It maps to the 'users' table in the database.
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    /**
     * Stores the user's role (e.g., "ROLE_USER", "ROLE_ADMIN").
     * This field is crucial for authorization.
     */
    @Column(name = "role", nullable = false, length = 50)
    private String role;


    // == UserDetails Methods Implementation ==

    /**
     * Returns the authorities granted to the user, based on their role.
     * @return a collection containing a single GrantedAuthority for the user's role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // The "ROLE_" prefix is a convention used by Spring Security for role-based authorization.
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    /**
     * Returns the password used to authenticate the user.
     * Spring Security will use this value to compare against the one provided during login.
     * @return the user's hashed password.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username used to authenticate the user. In our case, it's the 'name' field.
     * @return the user's name.
     */
    @Override
    public String getUsername() {
        return this.email;
    }


    // -- Account Status Methods --
    // For this basic setup, we will assume all accounts are always active and valid.

    /**
     * Indicates whether the user's account has expired.
     * @return {@code true} as accounts never expire in this setup.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * @return {@code true} as accounts are never locked in this setup.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * @return {@code true} as credentials never expire in this setup.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * @return {@code true} as users are always enabled in this setup.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}