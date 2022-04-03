package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ExitOverlayCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private Button closeButton;

  public boolean closeApp = true;

  /**
   * Constructor for ExitOverlayCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  public ExitOverlayCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the ExitOverlayCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Asks the user for confirmation
   */
  @FXML
  public void confirm() {
    mainCtrl.closeExitOverlay();
    if (closeApp) {
      mainCtrl.exit();
    } else {
      mainCtrl.playerExited = true;
      mainCtrl.prepareForNewGame();
      mainCtrl.backToMenu();
    }
  }

  /**
   * Closes the ExitOverlay
   */
  @FXML
  public void close() {
    mainCtrl.closeExitOverlay();
  }
}
