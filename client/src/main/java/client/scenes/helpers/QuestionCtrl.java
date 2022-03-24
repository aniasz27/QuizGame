package client.scenes.helpers;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

public abstract class QuestionCtrl {
  protected final ServerUtils server;
  protected final MainCtrl mainCtrl;

  @FXML
  public Line timer;

  public Timeline timerAnimation;

  @Inject
  public QuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  /**
   * Starts animation on progress bar which indicates time left
   */
  public void startTimer() {
    timer.setVisible(true);
    timer.setEndX(800);
    this.timerAnimation = new Timeline(
      new KeyFrame(Duration.seconds(10), new KeyValue(timer.endXProperty(), 0))
    );
    timerAnimation.setOnFinished(e -> timer.setVisible(false));
    timerAnimation.setCycleCount(1);
    timerAnimation.play();
  }

  /**
   * Requests to user to confirm exit
   *
   * @param actionEvent click on the button
   */
  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.playerExited = true;
    mainCtrl.openExitOverlay(false);
  }

  /**
   * Opens help
   *
   * @param actionEvent click on the button
   */
  @FXML
  private void help(ActionEvent actionEvent) {
    mainCtrl.openHelp();
  }

  /**
   * Display the question on the screen
   *
   * @param question to show
   */
  public abstract void displayQuestion(Question question);

  /**
   * Shows correct answer
   */
  public abstract void showCorrect();

  /**
   * Disables buttons after time runs out
   */
  public abstract void disableButtons();

  /**
   * Changes button to clearly indicate that a joker is used
   *
   * @param joker button to change
   */
  public void useJoker(Button joker) {
    joker.getStyleClass().add("used");
    joker.getStyleClass().remove("drop-shadow");
  }

  /**
   * Displays jokers on the question screen
   *
   * @param jokers Buttons to change
   */
  public void displayJokers(Button[] jokers) {
    for (int i = 0; i < 3; i++) {
      jokers[i].setDisable(false);
      if (mainCtrl.usedJokers[i]) {
        useJoker(jokers[i]);
      }
    }
    if (!mainCtrl.multiplayer) {
      jokers[1].setDisable(true);
    }
  }

  /**
   * Displays user's points on the screen
   */
  public void showPoints(Text points) {
    points.setText("Points: " + mainCtrl.getPoints());
  }

  /**
   * Set hover effect on emojis
   *
   * @param circle      of emojis
   * @param emojiGrid   of emojis
   * @param emojiButton of emojis
   * @param pane        of emojis
   */
  public static void hoverEffect(Circle circle, GridPane emojiGrid, Button emojiButton, StackPane pane) {
    emojiButton.setOnMouseEntered(event -> {
      pane.setVisible(true);
      circle.setVisible(true);
      emojiGrid.setVisible(true);
    });
    pane.setOnMouseExited(event -> {
      pane.setVisible(false);
      circle.setVisible(false);
      emojiGrid.setVisible(false);
    });
  }

  /**
   * Hides emojis on the screen
   *
   * @param circle    of emojis
   * @param emojiGrid of emojis
   * @param pane      of emojis
   */
  public static void displayEmojis(Circle circle, GridPane emojiGrid, StackPane pane) {
    pane.setVisible(false);
    circle.setVisible(false);
    emojiGrid.setVisible(false);
  }

  /**
   * Gives a hint to the user
   *
   * @param correct answer
   * @param buttons answers
   * @param hint    button
   */
  public void hintQ(boolean[] correct, Button[] buttons, Button hint) {
    if (mainCtrl.usedJokers[2]) {
      return;
    }
    Random random = new Random();
    int guess;
    do {
      guess = random.nextInt(3);
    } while (correct[guess]);
    buttons[guess].setDisable(true);
    useJoker(hint);
    mainCtrl.usedJokers[2] = true;
  }

  /**
   * Gives double Points to the user
   *
   * @param doublePts button
   * @return boolean
   */
  public boolean doublePoints(Button doublePts) {
    if (mainCtrl.usedJokers[0]) {
      return false;
    }
    useJoker(doublePts);
    mainCtrl.usedJokers[0] = true;
    return true;
  }

  public void decreaseTime() {
    double position = timer.getEndX() / 2.0;
    double time = 10.0 * position / 800.0;
    timer.setVisible(true);
    timer.setEndX(position);
    this.timerAnimation = new Timeline(
      new KeyFrame(Duration.seconds(time), new KeyValue(timer.endXProperty(), 0))
    );
    timerAnimation.setOnFinished(e -> timer.setVisible(false));
    timerAnimation.setCycleCount(1);
    timerAnimation.play();
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep((long) (time * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      disableButtons();
    });
    thread.start();
  }
}
