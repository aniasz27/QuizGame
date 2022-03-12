package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SplashCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

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
    mainCtrl.showWaitingRoom();
  }

  @FXML
  public void playSingleplayer() {
    mainCtrl.showSpWaitingRoom();
  }

  @FXML
  public void showAdmin() {
    mainCtrl.showActivityList();
  }


  @FXML
  public void back() {
    // TODO: disconnect from the server
    mainCtrl.showConnect();
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  /**
   * Client connects to the server for the first time
   */
  public void connect() {
    mainCtrl.clientId = server.connectFirst("ooo");
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
        server.keepAlive(mainCtrl.clientId);
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  public void stop() {
    EXEC.shutdownNow();
  }
}
