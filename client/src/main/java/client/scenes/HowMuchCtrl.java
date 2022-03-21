package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.HowMuchQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class HowMuchCtrl extends QuestionCtrl implements Initializable {
  @FXML
  private Button backButton;
  @FXML
  public StackPane imgContainer;
  @FXML
  private ImageView imageView;
  @FXML
  private Text description;
  @FXML
  private Button answer_1;
  @FXML
  private Button answer_2;
  @FXML
  private Button answer_3;
  @FXML
  private Text points;
  @FXML
  private Circle circle;
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
  private GridPane emojiGrid;
  @FXML
  private Button emojiButton;
  @FXML
  private StackPane pane;
  @FXML
  private Button doublePts;
  @FXML
  private Button hint;
  @FXML
  private Button minusTime;

  private Button[] buttons;
  private Label[] emojis;
  private Button[] jokers;

  private Activity activity;
  private HowMuchQuestion question;
  private boolean dbPoint;

  private boolean[] correct;
  private long[] answers;

  private Button clickedButton;

  @Inject
  public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttons = new Button[] {answer_1, answer_2, answer_3};
    emojis = new Label[] {emoji1, emoji2, emoji3, emoji4, emoji5};
    jokers = new Button[] {doublePts, hint, minusTime};
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
   * Display the question on the screen
   *
   * @param question to show
   */
  @Override
  public void displayQuestion(Question question) {
    pane.setVisible(false);
    circle.setVisible(false);
    emojiGrid.setVisible(false);
    this.clickedButton = null;
    this.dbPoint = false;
    this.question = (HowMuchQuestion) question;
    this.activity = this.question.getActivity();
    this.correct = this.question.getCorrect();
    this.answers = this.question.getAnswers();

    Rectangle clip = new Rectangle(
      imgContainer.getWidth(), imgContainer.getHeight()
    );
    clip.setArcWidth(20);
    clip.setArcHeight(20);
    imgContainer.setClip(clip);
    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    for (Button button : buttons) {
      button.getStyleClass().remove("good");
      button.getStyleClass().remove("bad");
      button.setDisable(false);
    }
    for (Button joker : jokers) {
      joker.setDisable(false);
    }
    for (int i = 0; i < 3; i++) {
      buttons[i].setText(answers[i] + " Wh");
      buttons[i].setUserData(correct[i]);
    }
    showPoints();
  }

  @Override
  public void showCorrect() {
    for (Button button : buttons) {
      button.getStyleClass().add((boolean) button.getUserData() ? "good" : "bad");
    }
    if (clickedButton != null && (boolean) clickedButton.getUserData()) {
      showUserCorrect();
    }
  }

  public void showUserCorrect() {
    int toAdd = mainCtrl.getPointsOffset();
    if (dbPoint) {
      toAdd *= 2;
    }
    mainCtrl.addPoints(toAdd);
    showPoints();
  }

  /**
   * Displays user points at the start of the question
   */
  public void showPoints() {
    points.setText("Points: " + mainCtrl.getPoints());
  }

  @FXML
  public void checkCorrectAnswer(MouseEvent event) {
    mainCtrl.stopPointsTimer();
    this.clickedButton = (Button) event.getSource();
    for (Button button : buttons) {
      button.setDisable(true);
    }
  }

  /**
   * Disable buttons in case when user does not pick an answer
   */
  @Override
  public void disableButtons() {
    for (Button button : buttons) {
      button.setDisable(true);
    }
    for (Button joker : jokers) {
      joker.setDisable(true);
    }
  }

  public void hint() {
    if (hint.getStyleClass().contains("used")) {
      return;
    }
    Random random = new Random();
    int guess;
    do {
      guess = random.nextInt(3);
    } while (correct[guess]);
    buttons[guess].setDisable(true);
    hint.getStyleClass().add("used");
    hint.getStyleClass().remove("drop-shadow");
  }

  public void doublePoints() {
    if (doublePts.getStyleClass().contains("used")) {
      return;
    }
    this.dbPoint = true;
    doublePts.getStyleClass().add("used");
    doublePts.getStyleClass().remove("drop-shadow");
  }
}
