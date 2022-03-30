package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScoreTest {

  Score score;

  @BeforeEach
  public void setUp() {
    score = new Score("Username", "Name", 0);
  }

  @Test
  public void constructorTest() {
    assertNotNull(score);
  }

  @Test
  public void getName() {
    assertEquals("Name", score.getName());
  }

  @Test
  public void getPlayer() {
    assertEquals("Username", score.getPlayer());
  }

  @Test
  public void getPoints() {
    assertEquals(0, score.getPoints());
  }

  @Test
  public void addPointsTest() {
    score.addPoints(100);
    assertEquals(100, score.getPoints());
  }

  @Test
  public void setPoints() {
    score.setPoints(1000);
    assertEquals(1000, score.getPoints());
  }

  @Test
  public void equalsSame() {
    assertEquals(score, score);
  }

  @Test
  public void equalsEqual() {
    String player = "Username";
    int points = 0;
    String name = "Name";
    Score score1 = new Score(player, name, points);
    assertEquals(score, score1);
  }

  @Test
  public void equalsNull() {
    assertNotEquals(score, null);
  }

  @Test
  public void equalsNotScore() {
    assertNotEquals(score, new Activity("a", "aa", "aaa", "aaaa", 0xAAAAA, "aaaaaa"));
  }

  @Test
  public void equalsDifferentPoints() {
    assertNotEquals(score, new Score("Username", "Name", 42));
  }

  @Test
  public void equalsDifferentId() {
    assertNotEquals(score, new Score("ID", "Name", 0));
  }

  @Test
  public void equalsDifferentUsername() {
    assertNotEquals(score, new Score("Username", "Username", 0));
  }

  @Test
  public void hashCodeSame() {
    assertEquals(score.hashCode(), score.hashCode());
  }

  @Test
  public void hashCodeEqual() {
    assertEquals(score.hashCode(), new Score("Username", "Name", 0).hashCode());
  }

  @Test
  public void hashCodeDifferent() {
    assertNotEquals(score.hashCode(), new Score("aaaaaaaa", "AAAAA", 0xAAAAAAA).hashCode());
  }

  @Test
  public void toStringTest() {
    String result = "Score{id='0', player='Username', points='0'}";
    assertEquals(result, score.toString());
  }
}
