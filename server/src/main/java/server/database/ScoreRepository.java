package server.database;

import commons.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScoreRepository extends JpaRepository<Score, Long> {

  @Query(value = "SELECT * FROM score s ORDER BY s.points DESC", nativeQuery = true)
  Iterable<Score> getLeaderboard();
}
