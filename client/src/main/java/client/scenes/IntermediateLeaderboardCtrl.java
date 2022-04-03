package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import commons.Score;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class IntermediateLeaderboardCtrl extends QuestionCtrl implements Initializable {
  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;

  /**
   * Constructor for IntermediateLeaderboardCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  public IntermediateLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  /**
   * Initializing the IntermediateLeaderboardCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  /**
   * Displays the emojis
   */
  public void display() {
    displayEmojis();
  }

  /**
   * Refreshes the leaderboard
   */
  public void refresh() {
    var leaderboard = server.getMultiLeaderboard(mainCtrl.serverIp, mainCtrl.gameId);
    List<Score> list = StreamSupport
      .stream(leaderboard.spliterator(), false)
      .sorted((Score scoreA, Score scoreB) -> Integer.compare(scoreB.points, scoreA.points))
      .collect(Collectors.toList());

    Platform.runLater(() -> MainCtrl.refreshLeaderboard(leaderboardDisplay, list));
  }

  /**
   * Goes back to SplashScreen
   *
   * @param actionEvent clicking on the back button
   */
  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showSplash();
  }

  /**
   * Displays the HelpOverlay
   *
   * @param actionEvent on clicking on the  button
   */
  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  // ignore following methods
  @Override
  public void displayQuestion(Question question) {

  }

  @Override
  public void showCorrect() {

  }
}
