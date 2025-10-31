package com.codewordle.codewordle.repository;

import com.codewordle.codewordle.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByUserAndStatus(User user, GameStatus status);
}
