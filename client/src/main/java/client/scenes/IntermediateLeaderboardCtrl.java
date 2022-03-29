package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import java.net.URL;
import java.util.ResourceBundle;
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

  @Inject
  public IntermediateLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  public void display() {
    displayEmojis();
  }

  public void refresh() {
    var leaderboard = server.getMultiLeaderboard(mainCtrl.serverIp, mainCtrl.gameId);

    Platform.runLater(() -> MainCtrl.refreshLeaderboard(leaderboardDisplay, leaderboard));
  }

  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showSplash();
  }

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
