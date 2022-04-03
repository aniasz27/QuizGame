package client.scenes;

import client.utils.ServerUtils;
import commons.Score;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

  /**
   * Constructor for EndScreenCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  public EndScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the EndScreenCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Goes back to SplashScreen
   */
  @FXML
  private void backToMenu() {
    mainCtrl.backToMenu();
  }

  /**
   * Starts the same game again based on multiplayer boolean
   */
  @FXML
  private void playAgain() {
    mainCtrl.prepareForNewGame();
    if (mainCtrl.multiplayer) {
      mainCtrl.showWaitingRoom();
    } else {
      mainCtrl.showSpWaitingRoom();
    }
  }

  /**
   * Refreshes the leaderboard
   */
  public void refresh() {
    Iterable<Score> scores;
    if (!mainCtrl.multiplayer) {
      scores = server.getSingleLeaderboard(mainCtrl.serverIp);
    } else {
      scores = server.getMultiLeaderboard(mainCtrl.serverIp, mainCtrl.gameId);
    }
    List<Score> list = StreamSupport
      .stream(scores.spliterator(), false)
      .sorted((Score scoreA, Score scoreB) -> Integer.compare(scoreB.points, scoreA.points))
      .collect(Collectors.toList());
    Platform.runLater(() -> MainCtrl.refreshLeaderboard(leaderboardDisplay, list));
  }
}
