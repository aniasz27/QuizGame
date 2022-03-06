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

public class WaitingRoomCtrl implements Initializable {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @Inject
  public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  private void back(ActionEvent actionEvent) {
    stop();
    mainCtrl.showConnect();
  }

  public void refresh() {
    // TODO: get list of players from server and display it
    //var quotes = server.getPlayers();
    //data = FXCollections.observableList(quotes);
    //table.setItems(data);
  }

  /**
   * Client connects to the server for the first time
   */
  public void connect() {
    server.setClientId(server.connectFirst("ooo"));
    keepAlive();
    checkGameActive();
  }

  private static ScheduledExecutorService EXECKeepAlive;

  /**
   * Sends http request from the client to the server every second
   */
  public void keepAlive() {
    EXECKeepAlive = Executors.newSingleThreadScheduledExecutor();
    EXECKeepAlive.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        server.keepAlive(server.getClientId());
      }
    }, 0, 1, TimeUnit.SECONDS);
  }


  private static ScheduledExecutorService EXECGameStarted;

  /**
   * Checks every second if a game containing the player has started
   */
  public void checkGameActive() {
    try {
      EXECGameStarted = Executors.newSingleThreadScheduledExecutor();
      EXECGameStarted.scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
          String gameId = server.isGameActive(server.getClientId());
          if (gameId != null) {
            server.setGameId(gameId);
            stop();
            mainCtrl.play();
          }
        }
      }, 0, 1, TimeUnit.SECONDS);
    } catch (Exception e) {
      System.out.println(e.getCause());
    }
  }

  public void stop() {
    EXECGameStarted.shutdownNow();
    EXECKeepAlive.shutdownNow();
  }

}
