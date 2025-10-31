package com.codewordle.codewordle.repository;

import com.codewordle.codewordle.model.Game;
import com.codewordle.codewordle.model.Guess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuessRepository extends JpaRepository<Guess, Long> {
    int countByGame(Game game);
    List<Guess> findByGameOrderByTimestampAsc(Game game);
}