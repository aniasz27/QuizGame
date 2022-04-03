package client.scenes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.utils.ServerUtils;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SplashCtrlTest {
  private MainCtrl sut;
  private ServerUtils server;

  private SplashCtrl splashCtrl;

  @BeforeEach
  public void setup() {
    sut = new MainCtrl(server);
    splashCtrl = new SplashCtrl(server, sut);
  }

  @Test
  public void playMultiplayerTest() {
    try {
      splashCtrl.playMultiplayer();
    } catch (NullPointerException e) {
      // expected, as some fxml elements have not been initialised
    }
    assertTrue(sut.multiplayer);
  }

  @Test
  public void playSingleplayerTest() {
    try {
      splashCtrl.playSingleplayer();
    } catch (NullPointerException e) {
      // expected, as some fxml elements have not been initialised
    }
    assertFalse(sut.multiplayer);
  }

  @Test
  public void backTest() {
    sut.keepAliveExec = Executors.newSingleThreadScheduledExecutor();
    try {
      splashCtrl.back();
    } catch (NullPointerException e) {
      // expected, as some fxml elements have not been initialised
    }
    assertTrue(sut.keepAliveExec.isShutdown());
  }
}
