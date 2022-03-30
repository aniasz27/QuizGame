package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import commons.Score;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreControllerTest {

  private TestScoreRepository repo;
  private ScoreController controller;

  @BeforeEach
  public void setup() {
    repo = new TestScoreRepository();
    controller = new ScoreController(repo);
  }

  @Test
  void getAll() {
    Score score = new Score("1234", "Player 1", 6);
    Score score2 = new Score("1234", "Player 1", 7);
    controller.addScore(score);
    controller.addScore(score2);
    Iterator<Score> list = controller.getAll().iterator();
    assertTrue(repo.calledMethods.contains("findAll"));
    assertEquals(score, list.next());
    assertEquals(score2, list.next());
  }

  @Test
  void getLeaderboard() {
    Score score = new Score("1234", "Player 1", 6);
    Score score2 = new Score("1234", "Player 1", 15);
    controller.addScore(score);
    controller.addScore(score2);
    Iterator<Score> list = controller.getLeaderboard().iterator();
    assertTrue(repo.calledMethods.contains("getLeaderboard"));
    assertEquals(score2, list.next());
    assertEquals(score, list.next());
  }

  @Test
  void addScore() {
    Score score = new Score("1234", "Player 1", 6);
    controller.addScore(score);
    assertTrue(repo.calledMethods.contains("save"));
    assertTrue(repo.scores.contains(score));
  }

  @Test
  void clear() {
    Score score = new Score("1234", "Player 1", 6);
    Score score2 = new Score("1234", "Player 1", 7);
    controller.addScore(score);
    controller.addScore(score2);
    controller.clear();
    assertTrue(repo.calledMethods.contains("deleteAll"));
    assertEquals(0, repo.scores.size());
  }
}