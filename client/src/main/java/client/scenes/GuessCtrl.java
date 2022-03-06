package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class GuessCtrl implements Initializable {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;


  @FXML
  private final ProgressBar timebar;

  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl, ProgressBar timebar) {
    this.server = server;
    this.mainCtrl = mainCtrl;
    this.timebar = timebar;
  }

  //unfinished
  public void start() throws InterruptedException {
    double progress = 0.0;
    for (int i = 0; i < 10; i++) {
      progress += 0.1;
      timebar.setProgress(progress);
      Thread.sleep(1000);
    }

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  //might need to change based on design decisions
  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showConnect();
  }

  @FXML
  private void submit(ActionEvent actionEvent) {
    //TODO
    //validate the answer
  }
}