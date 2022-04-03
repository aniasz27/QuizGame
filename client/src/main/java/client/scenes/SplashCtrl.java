package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class SplashCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private Button buttonMulti;
  @FXML
  private Button buttonSingle;

  /**
   * Constructor for SplashCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  public SplashCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the SplashCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Sets multiplayer to true and goes to waiting room
   */
  @FXML
  public void playMultiplayer() {
    mainCtrl.multiplayer = true;
    mainCtrl.showWaitingRoom();
  }

  /**
   * Sets the multiplayer to false and goes to SpWaitingRoom
   */
  @FXML
  public void playSingleplayer() {
    mainCtrl.multiplayer = false;
    mainCtrl.showSpWaitingRoom();
  }

  /**
   * Shows ActivityList
   */
  @FXML
  public void showAdmin() {
    mainCtrl.showActivityList();
  }

  /**
   * Shows the ConnectScreen
   */
  @FXML
  public void back() {
    mainCtrl.keepAliveExec.shutdownNow();
    mainCtrl.keepAliveExec = null;
    mainCtrl.showConnect();
  }

  /**
   * Goes to HelpOverlay
   *
   * @param actionEvent on Button Click
   */
  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }
}
