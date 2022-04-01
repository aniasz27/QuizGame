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

  @Inject
  public ExitOverlayCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void confirm() {
    mainCtrl.playerExited = true;
    mainCtrl.prepareForNewGame();
    mainCtrl.closeExitOverlay();
    if (closeApp) {
      mainCtrl.exit();
    } else {
      mainCtrl.backToMenu();
    }
  }

  @FXML
  public void close() {
    mainCtrl.closeExitOverlay();
  }
}
