package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WaitingRoomCtrl implements Initializable {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox playerListDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button startButton;

  @Inject
  public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void back(ActionEvent actionEvent) {
    mainCtrl.goBackToMenu();
  }

  @FXML
  public void start(ActionEvent actionEvent) {
    mainCtrl.showHowMuch();
  }

  /**
   * Displays a list of players from a hashmap.
   *
   * @param players the map of players to create the list from
   */
  public void refresh(Map<String, Integer> players) {
    // TODO: get list of players from server and display it
    //var quotes = server.getPlayers();
    //data = FXCollections.observableList(quotes);
    //table.setItems(data);
    if (players == null) {
      mainCtrl.showConnect();
      return;
    }

    // remove all players and re-add them
    playerListDisplay.getChildren().removeAll(playerListDisplay.getChildren());
    int[] i = {0};
    players.forEach((player, score) -> {
      Label l = new Label(player.equals(mainCtrl.name) ? player + " (You)" : player);
      l.getStyleClass().add("player");
      l.getStyleClass().add("border-bottom");
      if (i[0]++ == 0) {
        l.getStyleClass().add("player-top");
      }
      playerListDisplay.getChildren().add(l);
    });
  }
}
