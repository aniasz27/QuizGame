package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Score;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SpWaitingRoomCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;
  @FXML
  private Button startButton;

  @Inject
  public SpWaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void refresh() {
    Iterable<Score> scores = server.getSingleLeaderboard(mainCtrl.serverIp);
    MainCtrl.refreshLeaderboard(leaderboardDisplay, scores);
  }

  @FXML
  private void back(ActionEvent actionEvent) {
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
}
