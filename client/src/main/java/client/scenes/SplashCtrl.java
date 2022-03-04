package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SplashCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  private String clientId;

  @FXML
  private Button buttonMulti;
  @FXML
  private Button buttonSingle;

  @Inject
  public SplashCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void playMultiplayer() {
    mainCtrl.multiplayer = true;
    mainCtrl.showWaitingRoom();
  }

  @FXML
  public void playSingleplayer() {
    mainCtrl.multiplayer = false;
    mainCtrl.showHowMuch();
  }

  @FXML
  public void exit() {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Client connects to the server for the first time
   */
  public void connect() {
    this.clientId = server.connectFirst("ooo");
    keepAlive();
  }

  private static ScheduledExecutorService EXEC = Executors.newSingleThreadScheduledExecutor();

  /**
   * Sends http request from the client to the server every second
   */
  public void keepAlive() {
    EXEC.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        server.keepAlive(clientId);
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  public void stop() {
    EXEC.shutdownNow();
  }
}
