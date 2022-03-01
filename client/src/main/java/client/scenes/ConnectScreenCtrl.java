package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private Button playButton;
  @FXML
  private Button backButton;
  @FXML
  private TextField nameField;
  @FXML
  private TextField serverField;

  @Inject
  ConnectScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showSplash();
  }

  @FXML
  private void play(ActionEvent actionEvent) {
    if (mainCtrl.multiplayer) {
      mainCtrl.showWaitingRoom();
    } else {
      mainCtrl.play();
    }
  }
}
