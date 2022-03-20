package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ScoreTest {

  static Score score;

  @BeforeAll
  public static void setUp() {
    String player = "Username";
    String name = "Name";
    int points = 0;
    score = new Score(player, name, points);
  }

  @Test
  public void constructorTest() {
    assertNotNull(score);
  }

  @Test
  public void getPlayerTest() {
    assertEquals("Username", score.getPlayer());
  }


  @Test
  public void getPointsTest() {
    assertEquals(0, score.getPoints());
  }


  @Test
  public void addPointsTest() {
    score.addPoints(100);
    assertEquals(100, score.getPoints());
    score.points = 0;
  }

  @Test
  public void equalsSameTest() {
    assertEquals(score, score);
  }

  @Test
  public void equalsSameButDifferentTest() {
    String player = "Username";
    int points = 0;
    String name = "Name";
    Score score1 = new Score(player, name, points);
    assertEquals(score, score1);
  }

  @Test
  public void equalsDifferentTest() {
    String player = "Username";
    int points = 100;
    String name = "Name";
    Score score1 = new Score(player, name, points);
    assertFalse(score.equals(score1));
  }

  @Test
  public void toStringTest() {
    String result = "Score{id='0', player='Username', points='0'}";
    assertEquals(result, score.toString());
  }
}
