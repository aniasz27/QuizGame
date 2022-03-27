package client.scenes.helpers;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Emoji;
import commons.Joker;
import commons.Question;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
  private final Random notRandom = new Random();

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
    System.out.println("Exited question!");
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
    joker.getStyleClass().remove("drop-shadow");
  }

  /**
   * Displays jokers on the question screen
   */
  public void displayJokers() {
    for (int i = 0; i < 3; i++) {
      this.jokers[i].setDisable(false);
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
    if (!mainCtrl.multiplayer) {
      emojiButton.setVisible(false);
      return;
    }
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

  public Button getJokerElement(Joker joker) {
    switch (joker) {
      case DOUBLE:
        return doublePts;
      case TIME:
        return minusTime;
      case HINT:
      default:
        return hint;
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
    movingEmoji.setTranslateY(-350 + 100 * notRandom.nextDouble()); // vary height slightly

    // add emoji to scene (in line with the timer)
    root.getChildren().add(movingEmoji);

    Timeline emojiAnimation = new Timeline(
      new KeyFrame(Duration.seconds(6), new KeyValue(movingEmoji.translateXProperty(), 0)), // move
      new KeyFrame(Duration.seconds(6), new KeyValue(movingEmoji.opacityProperty(), 0)) // disappear
    );

    emojiAnimation.setOnFinished(e -> root.getChildren().remove(movingEmoji)); // remove emoji when finished
    emojiAnimation.setCycleCount(1);
    emojiAnimation.play();
  }

  public void showJoker(Joker joker) {
    System.out.println("SHOWN JOKER");
    String jokerText = getJokerElement(joker).getText();
    Label movingJoker = new Label(jokerText);
    movingJoker.setTranslateX(-1920.0 / 2.0);
    movingJoker.setTranslateY(-350 + 100 * notRandom.nextDouble());
    root.getChildren().add(movingJoker);
    Timeline jokerAnimation = new Timeline(
      new KeyFrame(Duration.seconds(6), new KeyValue(movingJoker.translateXProperty(), 0)),
      new KeyFrame(Duration.seconds(6), new KeyValue(movingJoker.opacityProperty(), 0))
    );
    jokerAnimation.setOnFinished(e -> root.getChildren().remove(movingJoker));
    jokerAnimation.setCycleCount(1);
    jokerAnimation.play();
  }

  /**
   * Hides emojis on the screen
   */
  public void displayEmojis() {
    this.pane.setVisible(false);
    this.circle.setVisible(false);
    this.emojiGrid.setVisible(false);
  }

  /**
   * Gives a hint to the user
   *
   * @param correct answer
   * @param buttons answers
   */
  public void hintQ(boolean[] correct, Button[] buttons) {
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
    mainCtrl.jokerWebSocket.sendMessage(Joker.HINT);
  }

  /**
   * Gives double Points to the user
   *
   * @return boolean
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

  public void initialize(URL location, ResourceBundle resources) {
    emojis = new Label[] {emoji1, emoji2, emoji3, emoji4, emoji5};
    jokers = new Button[] {doublePts, minusTime, hint};
    initializeEmojiEvents();
    hoverEffect();
  }
}
