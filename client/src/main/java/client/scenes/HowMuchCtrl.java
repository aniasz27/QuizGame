package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.HowMuchQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class HowMuchCtrl extends QuestionCtrl implements Initializable {

  @FXML
  private Button backButton;
  @FXML
  private ImageView imageView;

  //change the description based on activity
  @FXML
  private Text description;
  @FXML
  private Button answer_1;
  @FXML
  private Button answer_2;
  @FXML
  private Button answer_3;

  private Button[] buttons;


  private Activity activity;
  private HowMuchQuestion question;

  private boolean[] correct;
  private long[] answers;

  private Button clickedButton;

  @FXML
  private Image image;
  @FXML
  private Text points;

  @Inject
  public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttons = new Button[] {answer_1, answer_2, answer_3};
  }

  /**
   * Display the question on the screen
   *
   * @param question to show
   */
  @Override
  public void displayQuestion(Question question) {
    this.clickedButton = null;
    this.question = (HowMuchQuestion) question;
    this.activity = this.question.getActivity();
    this.correct = this.question.getCorrect();
    this.answers = this.question.getAnswers();

    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    for (Button button : buttons) {
      button.getStyleClass().remove("good");
      button.getStyleClass().remove("bad");
      button.setDisable(false);
    }
    for (int i = 0; i < 3; i++) {
      buttons[i].setText(answers[i] + "Wh");
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
    mainCtrl.addPoints(mainCtrl.getPointsOffset());
    showPoints();
  }

  /**
   * Displays user points at the start of the question
   */
  public void showPoints() {
    int userPoints = mainCtrl.getPoints();
    points.setText("Points: " + userPoints);
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
  }
}
