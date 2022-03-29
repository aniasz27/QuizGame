package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JokerMessageTest {
  @Test
  public void constructorTest() {
    JokerMessage message = new JokerMessage(Joker.TIME, "session", "clientId");
    assertNotNull(message);
    assertEquals(Joker.TIME, message.joker);
    assertEquals("session", message.gameSession);
    assertEquals("clientId", message.clientId);
  }
}