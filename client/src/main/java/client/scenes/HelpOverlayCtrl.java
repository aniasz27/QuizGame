package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class HelpOverlayCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private Button closeButton;

  /**
   * Constructor for HelpOverlayCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */

  @Inject
  public HelpOverlayCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Initializing the HelpOverlayCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  /**
   * Closes the help window
   */
  @FXML
  public void close() {
    mainCtrl.closeHelp();
  }
}
