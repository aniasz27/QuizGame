package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class IntermediateLeaderboardCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;
  @FXML
  private Circle circle;
  @FXML
  private Label emoji1;
  @FXML
  private Label emoji2;
  @FXML
  private Label emoji3;
  @FXML
  private Label emoji4;
  @FXML
  private Label emoji5;
  @FXML
  private GridPane emojiGrid;
  @FXML
  private Button emojiButton;
  @FXML
  private StackPane pane;

  private Label[] emojis;

  @Inject
  public IntermediateLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    emojis = new Label[] {emoji1, emoji2, emoji3, emoji4, emoji5};
    QuestionCtrl.hoverEffect(circle, emojiGrid, emojiButton, pane);
  }

  public void display() {
    QuestionCtrl.displayEmojis(circle, emojiGrid, pane);
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
}
