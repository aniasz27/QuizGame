package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.EstimateQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class GuessCtrl extends QuestionCtrl implements Initializable {
  private EstimateQuestion question;
  private Activity activity;

  @FXML
  public StackPane imgContainer;
  @FXML
  private ImageView imageView;
  @FXML
  private Text description;
  @FXML
  private TextField answer;
  @FXML
  private Button submit;
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

  private Label[] emojis;
  private boolean dbPoint;
  private Button[] jokers;
  private int point;

  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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
    this.hint.setDisable(true);
  }

  @Override
  public void displayQuestion(Question question) {
    pane.setVisible(false);
    circle.setVisible(false);
    emojiGrid.setVisible(false);
    this.dbPoint = false;
    this.question = (EstimateQuestion) question;
    this.activity = this.question.getActivity();
    this.submit.setDisable(false);
    this.answer.getStyleClass().remove("good");
    this.answer.getStyleClass().remove("bad");
    for (Button joker : jokers) {
      joker.setDisable(false);
    }

    Rectangle clip = new Rectangle(
      imgContainer.getWidth(), imgContainer.getHeight()
    );
    clip.setArcWidth(20);
    clip.setArcHeight(20);
    imgContainer.setClip(clip);
    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    points.setText("Points: " + mainCtrl.getPoints());
    answer.setText("Type in your answer");
    System.out.println(this.question.getAnswer());
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated and the correct score is shown
   */
  public void checkCorrect() {
    if (answer.getText() == "") {
      return;
    }
    int value = Integer.parseInt(answer.getText());
    point = (int) (question.calculateHowClose(value) * 100);
    submit.setDisable(true);
  }

  /**
   * Deletes the text upon mouse click
   */
  public void deleteText() {
    answer.setText("");
  }

  /**
   * Sets the color to green/red, shows the right answer and updates the points on the screen
   */
  public void showPoints() {
    points.setText("Points: " + mainCtrl.getPoints());
  }

  public void showCorrect() {
    answer.getStyleClass().add(point != 0 ? "good" : "bad");
    showPoints();
    answer.setText("Correct answer is: " + question.getAnswer());
  }

  /**
   * Disable button in case when user does not pick an answer
   */
  @Override
  public void disableButtons() {
    submit.setDisable(true);
    for (Button joker : jokers) {
      joker.setDisable(true);
    }
    if (dbPoint) {
      point *= 2;
    }
    mainCtrl.addPoints(point);
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
