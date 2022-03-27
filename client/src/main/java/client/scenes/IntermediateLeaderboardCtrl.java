package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class IntermediateLeaderboardCtrl extends QuestionCtrl implements Initializable {
  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;
  @FXML
  private Circle circle;
  @FXML
  private GridPane emojiGrid;
  @FXML
  private StackPane pane;

  @Inject
  public IntermediateLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  public void display() {
    QuestionCtrl.displayEmojis(circle, emojiGrid, pane);
  }

  public void refresh() {
    var leaderboard = server.getMultiLeaderboard(mainCtrl.serverIp, mainCtrl.gameId);

    leaderboardDisplay.getChildren().removeAll(leaderboardDisplay.getChildren());
    int[] i = {1};
    leaderboard.forEach(score -> {
      Label l = new Label(i[0] + ". " + score.getPlayer() + " " + score.getPoints());
      l.getStyleClass().add("list-item");
      l.getStyleClass().add("border-bottom");
      if (i[0]++ == 1) {
        l.getStyleClass().add("list-item-top");
      }
      leaderboardDisplay.getChildren().add(l);
    });
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

  @Override
  public void disableButtons() {

  }
}
