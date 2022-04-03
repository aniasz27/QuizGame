package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.HowMuchQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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


  private Button[] buttons;


  private Activity activity;
  private HowMuchQuestion question;
  private boolean dbPoint;

  private boolean[] correct;
  private long[] answers;

  private Button clickedButton;

  /**
   * Constructor for HowMuchCtrl
   *
   * @param server   server we are on
   * @param mainCtrl controller for the game flow
   */

  @Inject
  public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  /**
   * Initializing the HowMuchCtrl
   *
   * @param location  location
   * @param resources resources we're using
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    buttons = new Button[] {answer_1, answer_2, answer_3};
  }

  /**
   * Displays the HowMuch question screen
   *
   * @param question to show
   */
  @Override
  public void displayQuestion(Question question) {
    displayEmojis();
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
      button.getStyleClass().removeAll(Collections.singleton("good"));
      button.getStyleClass().removeAll(Collections.singleton("bad"));
      button.setDisable(false);
    }
    for (int i = 0; i < 3; i++) {
      buttons[i].setText(answers[i] + " Wh");
      buttons[i].setUserData(correct[i]);
    }
    showPoints();
    displayJokers();
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated
   */

  @Override
  public void showCorrect() {
    for (Button button : buttons) {
      button.getStyleClass().add((boolean) button.getUserData() ? "good" : "bad");
    }
    if (clickedButton != null && (boolean) clickedButton.getUserData()) {
      showUserCorrect();
    }
  }

  /**
   * Adds points to the user if they choose correct answer
   */
  public void showUserCorrect() {
    int toAdd = mainCtrl.getPointsOffset();
    if (dbPoint) {
      toAdd *= 2;
    }
    mainCtrl.addPoints(toAdd);
    showPoints();
  }

  /**
   * Stops timer after clicking on the button
   *
   * @param event click on the button
   */
  @FXML
  public void checkCorrectAnswer(MouseEvent event) {
    mainCtrl.stopPointsTimer();
    this.clickedButton = (Button) event.getSource();
    for (Button button : buttons) {
      button.setDisable(true);
    }
  }

  /**
   * Disables the buttons
   */

  @Override
  public void disableButtons() {
    super.disableButtons();
    for (Button button : buttons) {
      button.setDisable(true);
    }
  }

  /**
   * Activates Hint Joker
   */
  public void hint() {
    hintQ(correct, buttons);
  }

  /**
   * Activates DoublePoints Joker
   */
  public void doublePoints() {
    dbPoint = doublePointsQ();
  }
}
