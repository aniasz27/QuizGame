package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;
  private final Preferences prefs = Preferences.userNodeForPackage(client.scenes.ConnectScreenCtrl.class);

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    nameField.setText(prefs.get("name", ""));
  }

  @FXML
  public void exit() {
    mainCtrl.openExitOverlay(true);
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  public void refresh() {
    nameField.setText(prefs.get("name", ""));
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

      mainCtrl.name = server.getClient(mainCtrl.serverIp, mainCtrl.clientId).username;

      mainCtrl.keepAliveExec = Executors.newSingleThreadScheduledExecutor();
      mainCtrl.keepAliveExec.scheduleAtFixedRate(
        () -> {
          try {
            server.keepAlive(mainCtrl.serverIp, mainCtrl.clientId, mainCtrl.waitingForGame);
          } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(mainCtrl::showConnect);
            mainCtrl.reset();
          }
        },
        0,
        1,
        TimeUnit.SECONDS
      );

      prefs.put("name", nameField.getText());

      serverField.getStyleClass().removeAll(Collections.singleton("bad"));
      playButton.getStyleClass().removeAll(Collections.singleton("bad"));
      nameField.getStyleClass().removeAll(Collections.singleton("bad"));
      mainCtrl.showSplash();
    } catch (WebApplicationException e) {
      if (e.getResponse().getStatus() == 409) {
        nameField.getStyleClass().add("bad");
      }
      playButton.getStyleClass().add("bad");
    } catch (ProcessingException e) {
      serverField.getStyleClass().add("bad");
      playButton.getStyleClass().add("bad");
    }
  }

  @FXML
  private void resetBad() {
    serverField.getStyleClass().removeAll(Collections.singleton("bad"));
    playButton.getStyleClass().removeAll(Collections.singleton("bad"));
    nameField.getStyleClass().removeAll(Collections.singleton("bad"));
  }
}
