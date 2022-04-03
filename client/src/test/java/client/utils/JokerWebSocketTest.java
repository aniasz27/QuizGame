package client.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import client.scenes.MainCtrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JokerWebSocketTest {
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
    JokerWebSocket jokerWebSocket = new JokerWebSocket(mainCtrl, gameSession);

    assertNotNull(jokerWebSocket);
  }

  @Test
  public void equalsSameTest() {
    JokerWebSocket jokerWebSocket1 = new JokerWebSocket(mainCtrl, gameSession);
    JokerWebSocket jokerWebSocket2 = new JokerWebSocket(mainCtrl, gameSession);

    assertEquals(jokerWebSocket1, jokerWebSocket2);
  }

  @Test
  public void equalsDifferentMainCtrlTest() {
    JokerWebSocket jokerWebSocket1 = new JokerWebSocket(mainCtrl, gameSession);
    JokerWebSocket jokerWebSocket2 = new JokerWebSocket(new MainCtrl(new ServerUtils()), gameSession);

    assertNotEquals(jokerWebSocket1, jokerWebSocket2);
  }

  @Test
  public void equalsDifferentGameSessionTest() {
    JokerWebSocket jokerWebSocket1 = new JokerWebSocket(mainCtrl, gameSession);
    JokerWebSocket jokerWebSocket2 = new JokerWebSocket(mainCtrl, gameSession + "different");

    assertNotEquals(jokerWebSocket1, jokerWebSocket2);
  }

  @Test
  public void equalsDifferentTest() {
    JokerWebSocket jokerWebSocket1 = new JokerWebSocket(mainCtrl, gameSession);
    JokerWebSocket jokerWebSocket2 = new JokerWebSocket(new MainCtrl(new ServerUtils()), gameSession + "different");

    assertNotEquals(jokerWebSocket1, jokerWebSocket2);
  }
}
