package server.api;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import commons.Activity;
import commons.Client;
import commons.Score;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameControllerTest {

  private Random random = new Random();
  private GameController gameController;
  private PlayerController playerController;
  private TestScoreRepository repoScore;
  private AtomicReference<String> expected = new AtomicReference<>();

  private String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";

  @BeforeEach
  public void init() {
    TestActivityRepository repoActivity;
    repoActivity = new TestActivityRepository();
    List<Activity> activities = new ArrayList<>();
    for (int i = 0; i < 25; i++) {
      activities.add(new Activity("00-test" + i,
        "Group 13",
        "00/img",
        "Testing ",
        42 + i,
        "None"));
      activities.add(new Activity("", "", "",
        "very very very very very very very very very very very very long title", -5, ""));
    }
    repoScore = new TestScoreRepository();
    playerController = new PlayerController();
    playerController.addClient(null);
    playerController.addClient(null);
    playerController.clients.forEach((c, m) -> m.waitingForGame = true);
    repoActivity.setSortedActivities((ArrayList<Activity>) activities);
    gameController = new GameController(
      new ActivityController(repoActivity, random),
      random,
      playerController,
      new ScoreController(repoScore));
  }

  @Test
  public void constructorTest() {
    assertNotNull(gameController);
  }

  @Test
  public void playTest() {
    String gameId = gameController.play(true);
    assertTrue(gameId.matches(uuidRegex));
  }

  @Test
  public void scoreTest() throws InterruptedException {
    Client client = new Client("1234", "username", true);
    playerController.clients.put("1234", client);
    String gameId = gameController.playSingle("1234");
    gameController.games.get(0).questionCounter = 22;
    assertEquals(0, gameController.playerScore("1234"));
    gameController.playerScoreSend(gameId, new Score(client.id, client.username, 125));
    gameController.playerScoreUpdate("1234", 42);
    assertEquals(42, gameController.playerScore("1234"));
    assertEquals(-1, gameController.playerScore("00"));
    assertEquals(-1, gameController.playerScoreUpdate("00", 5));
    assertEquals(125, repoScore.scores.get(0).points);
  }

  @Test
  public void removePlayerTest() {
    gameController.removePlayer("", "1234");
    Client client = new Client("1234", "username", true);
    playerController.clients.put("1234", client);
    int count = playerController.getPlayerCounter();
    String gameId = gameController.play(true);
    assertEquals(count, gameController.games.get(0).players.size());
    gameController.removePlayer(gameId, "1234");
    assertEquals(count - 1, gameController.games.stream().filter(c -> c.id.equals(gameId)).findFirst()
      .get().players.size());
  }

  @Test
  public void nextTest() throws InterruptedException {
    String gameId = gameController.play(true);
    var res = gameController.next(gameId);
    Thread.sleep(500);
    assertTrue(res.hasResult());
  }

  @Test
  public void leaderboardTest() {
    String gameId = gameController.play(true);
    List<Client> clients = new ArrayList<>();
    for (Client client : gameController.games.get(0).players.keySet()) {
      clients.add(client);
    }
    for (int i = 0; i < clients.size(); i++) {
      gameController.playerScoreSend(gameId, new Score(clients.get(i).id, clients.get(i).username, 42 * (i + 1)));
    }
    Iterator<Score> scores = gameController.getMultiLeaderboard(gameId).iterator();
    for (int i = 0; i < clients.size(); i++) {
      var t = scores.next().points;
      System.out.println(t);
      assertEquals(42 * (i + 1), t);
    }
    assertNull(gameController.getMultiLeaderboard(""));
  }

}
