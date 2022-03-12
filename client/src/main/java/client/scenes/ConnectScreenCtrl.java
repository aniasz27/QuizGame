package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
  private Button helpButton;
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
  public void exit() {
    mainCtrl.openExitOverlay(true);
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  @FXML
  private void connect(ActionEvent actionEvent) {
    try {
      String ip = serverField.getText().trim();
      mainCtrl.serverIp = ip.isBlank()
        ? "http://localhost:8080/"
        : ip.matches("https?://.*") ? ip : "http://" + ip;

      mainCtrl.clientId = server.connect(
        mainCtrl.serverIp,
        nameField.getText().trim().equals("") ? null : nameField.getText().trim()
      );

      mainCtrl.keepAliveExec = Executors.newSingleThreadScheduledExecutor();
      mainCtrl.keepAliveExec.scheduleAtFixedRate(
        () -> server.keepAlive(mainCtrl.serverIp, mainCtrl.clientId),
        0,
        1,
        TimeUnit.SECONDS
      );

      nameField.getStyleClass().remove("bad");
      playButton.getStyleClass().remove("bad");

      mainCtrl.showSplash();
    } catch (WebApplicationException e) {
      if (e.getResponse().getStatus() == 409) {
        nameField.getStyleClass().add("bad");
      }
      playButton.getStyleClass().add("bad");
    }
  }

  private void play(ActionEvent actionEvent) {
    mainCtrl.name = nameField.getText();
    mainCtrl.showSplash();
  }
}
