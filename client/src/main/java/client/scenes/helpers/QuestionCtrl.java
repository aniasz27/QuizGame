package client.scenes.helpers;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Emoji;
import commons.EstimateQuestion;
import commons.InsteadOfQuestion;
import commons.Joker;
import commons.Question;
import java.net.URL;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

public abstract class QuestionCtrl {
  protected final ServerUtils server;
  protected final MainCtrl mainCtrl;

  @FXML
  private StackPane root;
  @FXML
  private Line timer;
  @FXML
  private Text points;
  @FXML
  private Circle circle;
  @FXML
  private GridPane emojiGrid;
  @FXML
  private Button emojiButton;
  @FXML
  private StackPane pane;
  @FXML
  private Label emoji1;
  @FXML
  private Label emoji2;
  @FXML
  private Label emoji3;
  @FXML
  private Label emoji4;
  @FXML
  private Label emoji5;
  @FXML
  protected Button doublePts;
  @FXML
  protected Button hint;
  @FXML
  protected Button minusTime;

  private Label[] emojis;
  private Button[] jokers;
  public Timeline timerAnimation;
  private final Random random = new Random();

  /**
   * Constructor for QuestionCtrl
   *
   * @param server   server we making it on
   * @param mainCtrl mainCtrl for the questions
   */
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
  public void disableButtons() {
    for (Button joker : jokers) {
      joker.setDisable(true);
    }
  }

  /**
   * Changes button to clearly indicate that a joker is used
   *
   * @param joker button to change
   */
  public void useJoker(Button joker) {
    joker.getStyleClass().add("used");
    joker.getStyleClass().removeAll(Collections.singleton("drop-shadow"));
  }

  /**
   * Changes button to clearly indicate that a joker is not used
   *
   * @param joker button to change
   */
  public void unusedJoker(Button joker) {
    joker.getStyleClass().add("drop-shadow");
    joker.getStyleClass().removeAll(Collections.singleton("used"));
  }

  /**
   * Displays jokers on the question screen
   */
  public void displayJokers() {
    for (int i = 0; i < 3; i++) {
      this.jokers[i].setDisable(false);
      unusedJoker(this.jokers[i]);
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
  public void showPoints() {
    this.points.setText("Points: " + mainCtrl.getPoints());
  }

  /**
   * Set hover effect on emojis
   */
  public void hoverEffect() {
    this.emojiButton.setOnMouseEntered(event -> {
      this.pane.setVisible(true);
      this.circle.setVisible(true);
      this.emojiGrid.setVisible(true);
    });
    this.pane.setOnMouseExited(event -> {
      this.pane.setVisible(false);
      this.circle.setVisible(false);
      this.emojiGrid.setVisible(false);
    });
  }

  /**
   * Returns which label element corresponds to a given emoji value
   *
   * @param emoji value type
   * @return label corresponding to emoji
   */
  public Label getEmojiElement(Emoji emoji) {
    switch (emoji) {
      case HAPPY:
        return emoji1;
      case ANGRY:
        return emoji2;
      case SAD:
        return emoji3;
      case DEAD:
        return emoji4;
      case STARE:
      default:
        return emoji5; // STARE is the default emoji
    }
  }

  /**
   * Return text on the joker to show
   *
   * @param joker to show appropriate text
   * @return Button to show
   */
  public Pair<Button, Double> getJokerElement(Joker joker) {
    switch (joker) {
      case DOUBLE:
        return new Pair<>(doublePts, -300.0);
      case TIME:
        return new Pair<>(minusTime, -325.0);
      case HINT:
      default:
        return new Pair<>(hint, -275.0);
    }
  }

  /**
   * Initializes what emoji value should be sent via websockets for every emoji label clicked
   */
  public void initializeEmojiEvents() {
    for (Emoji emojiValue : Emoji.values()) {
      Label emojiElement = getEmojiElement(emojiValue);
      emojiElement.setOnMouseClicked((e) -> mainCtrl.emojiWebSocket.sendMessage(emojiValue)
      );
    }
  }

  /**
   * Shows emoji on the screen
   *
   * @param emoji to show
   */
  public void showEmoji(Emoji emoji) {
    System.out.println("SHOWN EMOJI ");
    // create emoji label
    String emojiText = getEmojiElement(emoji).getText();
    Label movingEmoji = new Label(emojiText);
    movingEmoji.getStyleClass().addAll("icon", "emoji");

    // set initial position
    movingEmoji.setTranslateX(1920 / 2.0);
    makeAnimation(movingEmoji, -350 + 100 * random.nextDouble());
  }

  /**
   * Makes animation for a Label
   *
   * @param movingElement element to animate
   * @param height        of the element
   */
  public void makeAnimation(Label movingElement, double height) {
    movingElement.setTranslateY(height); // vary height slightly
    // add emoji to scene (in line with the timer)
    root.getChildren().add(movingElement);
    Timeline animation = new Timeline(
      new KeyFrame(Duration.seconds(6), new KeyValue(movingElement.translateXProperty(), 0)), // move
      new KeyFrame(Duration.seconds(6), new KeyValue(movingElement.opacityProperty(), 0)) // disappear
    );
    animation.setOnFinished(e -> root.getChildren().remove(movingElement)); // remove emoji when finished
    animation.setCycleCount(1);
    animation.play();
  }

  /**
   * Shows joker on the screen
   *
   * @param joker to show
   */
  public void showJoker(Joker joker) {
    System.out.println("SHOWN JOKER");
    Pair<Button, Double> pair = getJokerElement(joker);
    Label movingJoker = new Label(pair.getKey().getText());
    movingJoker.setTranslateX(-1920.0 / 2.0);
    makeAnimation(movingJoker, pair.getValue());
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated
   *
   * @param answer   textField to get answer from
   * @param question Question (EstimateQuestion or InsteadOfQuestion)
   * @param submit   Button
   * @return points to add
   */
  public int checkCorrect(TextField answer, Question question, Button submit) {
    if (answer.getText().equals("")) {
      return 0;
    }
    long value;
    try {
      value = Long.parseLong(answer.getText());
    } catch (NumberFormatException nfe) {
      answer.setText("Not a number");
      return 0;
    }
    mainCtrl.stopPointsTimer();
    submit.setDisable(true);
    answer.setDisable(true);
    if (question instanceof InsteadOfQuestion) {
      return (int) (((InsteadOfQuestion) question).calculateHowClose(value) * mainCtrl.getPointsOffset());
    }
    return (int) (((EstimateQuestion) question).calculateHowClose(value) * mainCtrl.getPointsOffset());
  }

  /**
   * Hides emojis on the screen
   */
  public void displayEmojis() {
    if (!mainCtrl.multiplayer) {
      emojiButton.setVisible(false);
    }
    this.pane.setVisible(false);
    this.circle.setVisible(false);
    this.emojiGrid.setVisible(false);
  }

  /**
   * Shows the user the range in which the answer is
   *
   * @param field  in which range is indicated
   * @param answer correct answer
   */
  public void hintR(TextField field, long answer) {
    if (mainCtrl.usedJokers[2]) {
      return;
    }
    int range = random.nextInt(50);
    long leftBound = (long) (answer * (1 - range / 100.0));
    range = random.nextInt(50);
    long rightBound = (long) (answer * (1 + range / 100.0));
    field.setText("Range: " + leftBound + " - " + rightBound);
    field.setPromptText("Range: " + leftBound + " - " + rightBound);
    useJoker(hint);
    mainCtrl.usedJokers[2] = true;
    mainCtrl.jokerWebSocket.sendMessage(Joker.HINT);
  }

  /**
   * Disables wrong answer
   *
   * @param correct answer
   * @param buttons answers
   */
  public void hintQ(boolean[] correct, Button[] buttons) {
    if (mainCtrl.usedJokers[2]) {
      return;
    }
    int guess;
    do {
      guess = random.nextInt(3);
    } while (correct[guess]);
    buttons[guess].setDisable(true);
    useJoker(hint);
    mainCtrl.usedJokers[2] = true;
    mainCtrl.jokerWebSocket.sendMessage(Joker.HINT);
  }

  /**
   * Gives double Points to the user
   *
   * @return boolean to double points
   */
  public boolean doublePointsQ() {
    if (mainCtrl.usedJokers[0]) {
      return false;
    }
    useJoker(doublePts);
    mainCtrl.usedJokers[0] = true;
    mainCtrl.jokerWebSocket.sendMessage(Joker.DOUBLE);
    return true;
  }

  /**
   * Sends message to other users to reduce time
   */
  public void decreaseTime() {
    if (mainCtrl.usedJokers[1]) {
      return;
    }
    useJoker(minusTime);
    mainCtrl.usedJokers[1] = true;
    mainCtrl.jokerWebSocket.sendMessage(Joker.TIME);
  }

  /**
   * Reduces time on the screen
   */
  public void reduceTime() {
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

  /**
   * Initializes question screen
   *
   * @param location  URL
   * @param resources ResourceBundle
   */
  public void initialize(URL location, ResourceBundle resources) {
    emojis = new Label[] {emoji1, emoji2, emoji3, emoji4, emoji5};
    jokers = new Button[] {doublePts, minusTime, hint};
    initializeEmojiEvents();
    hoverEffect();
  }
}
