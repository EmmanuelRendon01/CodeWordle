package com.codewordle.codewordle.repository;

import com.codewordle.codewordle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Finds a user by their unique name.
     * @param email The name of the user to find.
     * @return an Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findByEmail(String email);
}
