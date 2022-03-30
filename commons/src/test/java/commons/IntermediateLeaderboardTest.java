package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class IntermediateLeaderboardTest {
  @Test
  public void constructorTest() {
    IntermediateLeaderboardQuestion intermediateLeaderboard = new IntermediateLeaderboardQuestion();
    assertNotNull(intermediateLeaderboard);
    assertEquals(Question.Type.INTERLEADERBOARD, intermediateLeaderboard.type);
  }
}