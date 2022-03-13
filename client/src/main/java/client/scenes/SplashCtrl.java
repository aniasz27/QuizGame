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


  @Inject
  public SplashCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void playMultiplayer() {
    mainCtrl.showWaitingRoom();
  }

  @FXML
  public void playSingleplayer() {
    mainCtrl.showSpWaitingRoom();
  }

  @FXML
  public void showAdmin() {
    mainCtrl.showActivityList();
  }


  @FXML
  public void back() {
    mainCtrl.keepAliveExec.shutdownNow();
    mainCtrl.keepAliveExec = null;
    mainCtrl.showConnect();
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }
}
