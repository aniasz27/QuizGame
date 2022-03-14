package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.EstimateQuestion;
import commons.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


public class GuessCtrl extends QuestionCtrl implements Initializable {
  private EstimateQuestion question;
  private Activity activity;

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

  private int correctAnswer = 1;

  @FXML
  private Text points;


  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //get the activity to set the required fields - correct answer, question, image
    showImage("");
  }

  @FXML
  public void showImage(String imageName) {
    image = new Image(getClass().getResourceAsStream(imageName));
    imageView.setImage(image);
  }

  @FXML
  private void submit(ActionEvent actionEvent) {
    String userAnswer = answer.getText();
    char[] check = userAnswer.toCharArray();
    boolean rightInput = true;
    for (int i = 0; i < check.length; i++) {
      Character current = check[i];
      if (!Character.isDigit(current)) {
        rightInput = false;
        break;
      }
    }
    if (!rightInput) {
      answer.setText("Incorrect input format");
    } else {
      submit.setDisable(true);
      int answer = Integer.parseInt(userAnswer);
      if (answer >= correctAnswer * 0.8 && answer <= correctAnswer * 1.2) {
        showCorrect();
      } else {
        showIncorrect();
      }
    }
    //TODO
    //validate the answer
  }


  @Override
  public void displayQuestion(Question question) {
    this.question = (EstimateQuestion) question;
    this.activity = this.question.getActivity();
    imageView = new ImageView(getClass().getResource(activity.getImage_path()).toExternalForm());
    description.setText(activity.getTitle());
  }

  public void checkCorrect() {
    int value = Integer.parseInt(answer.getText());
    int point = (int) (question.calculateHowClose(value) * 100);
    mainCtrl.addPoints(point);
  }

  /**
   * Displays user points at the start of the question
   */

  public void showPoints() {
    int userPoints = server.playerScore(mainCtrl.serverIp, mainCtrl.clientId);
    points.setText("Points: " + userPoints);
  }

  //sets the textfield color to green and increases the points
  //shows the correct answer
  public void showCorrect() {
    answer.getStyleClass().add("good");
    //TODO
    //increase the points
    answer.setText("Correct answer is: " + correctAnswer);
  }

  //sets the textfield color to red to indicate the incorrect answer
  // shows the correct answer
  public void showIncorrect() {
    answer.getStyleClass().add("bad");
    answer.setText("Correct answer is: " + correctAnswer);
  }

}