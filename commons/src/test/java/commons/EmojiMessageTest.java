package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EmojiMessageTest {
  @Test
  public void constructorTest() {
    EmojiMessage message = new EmojiMessage(Emoji.STARE, "session");
    assertNotNull(message);
    assertEquals(Emoji.STARE, message.emoji);
    assertEquals("session", message.gameSession);
  }
}