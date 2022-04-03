package client.scenes;

import client.utils.GameUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WaitingRoomCtrl implements Initializable {
  private final ServerUtils server;
  private final GameUtils gameUtils;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox playerListDisplay;
  @FXML
  private Button backButton;
  @FXML
  private Button helpButton;
  @FXML
  private Button startButton;
  @FXML
  private Text playerCounterField;

  /**
   * Constructor for WaitingRoomCtrl
   *
   * @param server    server we are on
   * @param gameUtils gameUtils
   * @param mainCtrl  controller for the game flow
   */
  @Inject
  public WaitingRoomCtrl(ServerUtils server, GameUtils gameUtils, MainCtrl mainCtrl) {
    this.server = server;
    this.gameUtils = gameUtils;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the WaitingRoomCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Goes back to SplashScreen
   *
   * @param actionEvent on Button Click
   */

  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.waitingForGame = false;
    server.stopUpdates();
    mainCtrl.showSplash();
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

  /**
   * Starts the game
   *
   * @param actionEvent on Button Click
   */
  @FXML
  private void play(ActionEvent actionEvent) {
    server.stopUpdates();
    server.startGame(mainCtrl.serverIp, true);
  }

  /**
   * Displays a list of players in the lobby.
   */
  public void refresh() {
    // only include players that are marked as 'waitingForGame'
    var players = server.getPlayers(mainCtrl.serverIp).stream().filter(c -> c.waitingForGame)
      .collect(Collectors.toList());

    startButton.setDisable(players.size() < 2);
    playerCounterField.setText(String.valueOf(players.size()));

    // remove all players and re-add them
    playerListDisplay.getChildren().removeAll(playerListDisplay.getChildren());
    int[] i = {0};
    players.forEach(player -> {
      Label l = new Label(
        player.username.equals(server.getClient(mainCtrl.serverIp, mainCtrl.clientId).username)
          ? "You (" + player.username + ")"
          : player.username
      );
      l.getStyleClass().add("list-item");
      l.getStyleClass().add("border-bottom");
      if (i[0]++ == 0) {
        l.getStyleClass().add("list-item-top");
      }
      playerListDisplay.getChildren().add(l);
    });

    gameUtils.isActive(mainCtrl.serverIp, mainCtrl.clientId);
  }

  /**
   * Call long polling method to update the waiting room automatically
   */
  public void listenForNewPlayers() {
    server.registerForPlayerUpdates(mainCtrl.serverIp, np -> Platform.runLater(this::refresh));
  }
}
