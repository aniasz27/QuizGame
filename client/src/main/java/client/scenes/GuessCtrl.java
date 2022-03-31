package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.EstimateQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class GuessCtrl extends QuestionCtrl implements Initializable {
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

  private boolean dbPoint;
  private int point;
  private EstimateQuestion question;
  private Activity activity;

  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  /**
   * Displays question on the screen
   *
   * @param question to display
   */
  @Override
  public void displayQuestion(Question question) {
    displayEmojis();
    this.dbPoint = false;
    this.question = (EstimateQuestion) question;
    this.activity = this.question.getActivity();
    this.submit.setDisable(false);
    this.answer.getStyleClass().removeAll(Collections.singleton("good"));
    this.answer.getStyleClass().removeAll(Collections.singleton("bad"));
    displayJokers();
    this.hint.setDisable(true);

    Rectangle clip = new Rectangle(
      imgContainer.getWidth(), imgContainer.getHeight()
    );
    clip.setArcWidth(20);
    clip.setArcHeight(20);
    imgContainer.setClip(clip);
    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    showPoints();
    answer.setText("Type in your answer");
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated
   */
  public void checkCorrect() {
    mainCtrl.stopPointsTimer();
    if (answer.getText().equals("")) {
      return;
    }
    long value;
    try {
      value = Long.parseLong(answer.getText());
    } catch (NumberFormatException nfe) {
      answer.setText("Not a number");
      return;
    }
    point = (int) (question.calculateHowClose(value) * mainCtrl.getPointsOffset() / 100);
    submit.setDisable(true);
  }

  /**
   * Deletes the text upon mouse click
   */
  public void deleteText() {
    answer.setText("");
  }

  @Override
  public void showCorrect() {
    answer.getStyleClass().add(point != 0 ? "good" : "bad");
    if (dbPoint) {
      point *= 2;
    }
    mainCtrl.addPoints(point);
    showPoints();
    answer.setText("Correct answer is: " + question.getAnswer());
  }

  @Override
  public void disableButtons() {
    submit.setDisable(true);
    super.disableButtons();
  }

  public void doublePoints() {
    dbPoint = doublePointsQ();
  }
}
