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

public class IntermediateLeaderboardCtrl {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox leaderboardDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;

  @Inject
  public IntermediateLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
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


}
