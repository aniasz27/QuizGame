package client.scenes.helpers;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public abstract class QuestionCtrl {
  protected final ServerUtils server;
  protected final MainCtrl mainCtrl;

  @FXML
  public Line timer;

  @Inject
  public QuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  public void startTimer() {
    timer.setVisible(true);
    timer.setEndX(800);
    Timeline timerAnimation = new Timeline(
      new KeyFrame(Duration.seconds(10), new KeyValue(timer.endXProperty(), 0))
    );
    timerAnimation.setOnFinished(e -> timer.setVisible(false));
    timerAnimation.setCycleCount(1);
    timerAnimation.play();
  }

  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.playerExited = true;
    mainCtrl.openExitOverlay(false);
  }

  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  public abstract void displayQuestion(Question question);

  public abstract void showCorrect();

  public abstract void disableButtons();
}
