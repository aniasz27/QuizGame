package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Score;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

  /**
   * Constructor for SpWaitingRoomCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  public SpWaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the SpWaitingRoomCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Refreshes the Leaderboard
   */
  public void refresh() {
    Iterable<Score> scores = server.getSingleLeaderboard(mainCtrl.serverIp);
    Platform.runLater(() -> MainCtrl.refreshLeaderboard(leaderboardDisplay, scores));
  }

  /**
   * Goes back to SplashScreen
   *
   * @param actionEvent on Button Click
   */
  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showSplash();
  }

  /**
   * Starts the game
   *
   * @param actionEvent on Button Click
   */
  @FXML
  public void start(ActionEvent actionEvent) {
    mainCtrl.start();
  }

  /**
   * Opens HelpOverlay
   *
   * @param actionEvent on Button Click
   */
  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }
}
