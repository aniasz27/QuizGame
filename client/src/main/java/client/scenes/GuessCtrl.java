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
  private int point = 0;
  private EstimateQuestion question;
  private Activity activity;

  /**
   * Constructor for GuessCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */
  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  /**
   * Initializing the GuessCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */
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
    this.answer.setDisable(false);
    this.answer.getStyleClass().removeAll(Collections.singleton("good"));
    this.answer.getStyleClass().removeAll(Collections.singleton("bad"));
    this.point = 0;
    displayJokers();

    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    showPoints();
    answer.setText("Type in your answer");
    answer.setPromptText("Type in your answer");
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated
   */
  public void checkCorrect() {
    point = checkCorrect(answer, question, submit);
  }

  /**
   * Deletes the text upon mouse click
   */
  public void deleteText() {
    answer.setText("");
  }

  /**
   * Shows the correct answer and adds points
   */
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

  /**
   * Disables the buttons
   */
  @Override
  public void disableButtons() {
    super.disableButtons();
    submit.setDisable(true);
    answer.setDisable(true);
  }

  /**
   * Double points joker
   */
  public void doublePoints() {
    dbPoint = doublePointsQ();
  }

  public void hint() {
    hintR(answer, this.activity.consumption_in_wh);
  }
}
