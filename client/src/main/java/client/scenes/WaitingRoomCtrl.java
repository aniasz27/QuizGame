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
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WaitingRoomCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox playerListDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;
  @FXML
  private Button startButton;

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
    mainCtrl.showSplash();
  }

  @FXML
  public void start(ActionEvent actionEvent) {
    mainCtrl.start();
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  @FXML
  private void play(ActionEvent actionEvent) throws InterruptedException {
    mainCtrl.play();
  }

  /**
   * Displays a list of players in the lobby.
   */
  public void refresh() {
    var players = server.getPlayers(mainCtrl.serverIp);

    startButton.setDisable(players.size() < 2);

    // remove all players and re-add them
    playerListDisplay.getChildren().removeAll(playerListDisplay.getChildren());
    int[] i = {0};
    players.forEach(player -> {
      Label l = new Label(
        player.equals(server.getName(mainCtrl.serverIp, mainCtrl.clientId))
          ? "You (" + player + ")"
          : player
      );
      l.getStyleClass().add("list-item");
      l.getStyleClass().add("border-bottom");
      if (i[0]++ == 0) {
        l.getStyleClass().add("list-item-top");
      }
      playerListDisplay.getChildren().add(l);
    });
  }

  private static ScheduledExecutorService EXECGameStarted = Executors.newSingleThreadScheduledExecutor();

  /**
   * Checks every second if a game containing the player has started
   */
  public void checkGameActive() {
    try {
      EXECGameStarted = Executors.newSingleThreadScheduledExecutor();
      EXECGameStarted.scheduleAtFixedRate(() -> {
        String gameId = server.isGameActive(mainCtrl.serverIp, mainCtrl.clientId);
        if (!gameId.equals("")) {
          mainCtrl.gameId = gameId;
          stop();
          try {
            mainCtrl.play();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }, 0, 1, TimeUnit.SECONDS);
    } catch (Exception e) {
      System.out.println(e.getCause().toString());
    }
  }

  public void stop() {
    EXECGameStarted.shutdownNow();
  }

}
