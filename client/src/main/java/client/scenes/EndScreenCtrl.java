package client.scenes;

import client.utils.ServerUtils;
import commons.Score;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

public class EndScreenCtrl implements Initializable {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button playAgainButton;
  @FXML
  private Button backToMenuButton;

  @Inject
  public EndScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  private void backToMenu() {
    mainCtrl.backToMenu();
  }

  @FXML
  private void playAgain() {
    if (mainCtrl.multiplayer) {
      mainCtrl.showWaitingRoom();
    } else {
      mainCtrl.showSpWaitingRoom();
    }
  }

  public void refresh() {
    Iterable<Score> scores = server.getSingleLeaderboard(mainCtrl.serverIp);
    MainCtrl.refreshLeaderboard(leaderboardDisplay, scores);
  }
}
