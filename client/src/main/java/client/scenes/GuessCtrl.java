package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.EstimateQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
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
  private Image image;

  @FXML
  private Button submit;


  @FXML
  private Text points;

  private boolean correct;


  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @Override
  public void displayQuestion(Question question) {
    this.correct = false;
    this.question = (EstimateQuestion) question;
    this.activity = this.question.getActivity();
    this.submit.setDisable(false);
    this.answer.getStyleClass().remove("good");
    this.answer.getStyleClass().remove("bad");

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
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated and the correct score is shown
   */
  public void checkCorrect() {
    int value = Integer.parseInt(answer.getText());
    int point = (int) (question.calculateHowClose(value) * 100);
    submit.setDisable(true);
    if (point != 0) {
      correct = true;
      mainCtrl.addPoints(point);
    }
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
    int userPoints = mainCtrl.getPoints();
    points.setText("Points: " + userPoints);
  }

  public void showCorrect() {
    answer.getStyleClass().add(correct ? "good" : "bad");
    showPoints();
    answer.setText("Correct answer is: " + question.getAnswer());
  }

  /**
   * Disable button in case when user does not pick an answer
   */
  @Override
  public void disableButtons() {
    submit.setDisable(true);
  }
}
