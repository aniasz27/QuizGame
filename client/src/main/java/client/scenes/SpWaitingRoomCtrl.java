package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    var leaderboard = server.getLeaderboard();

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
  public void start(ActionEvent actionEvent) {
    mainCtrl.start();
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  @FXML
  private void play(ActionEvent actionEvent) throws InterruptedException {
    mainCtrl.showHowMuch();
  }
}
