package client.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import client.scenes.MainCtrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmojiWebSocketTest {
  private MainCtrl mainCtrl;
  private ServerUtils server;
  private String gameSession;

  @BeforeEach
  public void setup() {
    mainCtrl = new MainCtrl(server);
    gameSession = "gameSession";
  }

  @Test
  public void constructorTest() {
    EmojiWebSocket emojiWebSocket = new EmojiWebSocket(mainCtrl, gameSession);

    assertNotNull(emojiWebSocket);
  }

  @Test
  public void equalsSameTest() {
    EmojiWebSocket emojiWebSocket1 = new EmojiWebSocket(mainCtrl, gameSession);
    EmojiWebSocket emojiWebSocket2 = new EmojiWebSocket(mainCtrl, gameSession);

    assertEquals(emojiWebSocket1, emojiWebSocket2);
  }

  @Test
  public void equalsDifferentMainCtrlTest() {
    EmojiWebSocket emojiWebSocket1 = new EmojiWebSocket(mainCtrl, gameSession);
    EmojiWebSocket emojiWebSocket2 = new EmojiWebSocket(new MainCtrl(new ServerUtils()), gameSession);

    assertNotEquals(emojiWebSocket1, emojiWebSocket2);
  }

  @Test
  public void equalsDifferentGameSessionTest() {
    EmojiWebSocket emojiWebSocket1 = new EmojiWebSocket(mainCtrl, gameSession);
    EmojiWebSocket emojiWebSocket2 = new EmojiWebSocket(mainCtrl, gameSession + "different");

    assertNotEquals(emojiWebSocket1, emojiWebSocket2);
  }

  @Test
  public void equalsDifferentTest() {
    EmojiWebSocket emojiWebSocket1 = new EmojiWebSocket(mainCtrl, gameSession);
    EmojiWebSocket emojiWebSocket2 = new EmojiWebSocket(new MainCtrl(new ServerUtils()), gameSession + "different");

    assertNotEquals(emojiWebSocket1, emojiWebSocket2);
  }
}
