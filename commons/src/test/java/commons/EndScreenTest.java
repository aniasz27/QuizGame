package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EndScreenTest {
  @Test
  public void constructorTest() {
    EndScreen endScreen = new EndScreen();
    assertNotNull(endScreen);
    assertEquals(Question.Type.ENDSCREEN, endScreen.type);
  }
}