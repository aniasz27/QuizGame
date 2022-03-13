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
  private void playAgain() throws InterruptedException {
    //TODO check if multiplayer or singleplayer
    mainCtrl.showWaitingRoom();
  }

  public void refresh() {
  
    Iterable<Score> scores = server.getSingleLeaderboard(mainCtrl.serverIp);
    
    leaderboardDisplay.getChildren().removeAll(leaderboardDisplay.getChildren());

    final boolean[] first = {true};

    scores.forEach(s -> {
      Label label = new Label(s.getPlayer());
      label.getStyleClass().add("expand");
      label.getStyleClass().add("list-item");
      label.getStyleClass().add("border-bottom");
      if (first[0]) {
        label.getStyleClass().add("list-item-top-left");
      }
      HBox score = new HBox();
      HBox.setHgrow(label, Priority.ALWAYS);
      score.getChildren().add(label);
      label = new Label(String.valueOf(s.getPoints()));
      label.getStyleClass().add("expand");
      label.getStyleClass().add("list-item");
      label.getStyleClass().add("border-bottom");
      if (first[0]) {
        label.getStyleClass().add("list-item-top-right");
        first[0] = false;
      }
      score.getChildren().add(label);
      leaderboardDisplay.getChildren().add(score);
    });

  }
}
