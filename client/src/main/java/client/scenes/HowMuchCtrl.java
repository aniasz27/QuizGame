package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.HowMuchQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class HowMuchCtrl extends QuestionCtrl {

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


  private Activity activity;
  private HowMuchQuestion question;

  private boolean[] buttons;

  private int chosen;

  @FXML
  private Image image;
  @FXML
  private Text points;

  //array of String to symbolize which button has the correct answer
  //entries correspond to the style classes to be added
  private String[] correctness;

  @Inject
  public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @FXML

  @Override
  public void displayQuestion(Question question) {
    this.buttons = new boolean[3];
    this.question = (HowMuchQuestion) question;
    this.activity = this.question.getActivity();

    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    description.setText(activity.getTitle());
    answer_1.setDisable(false);
    answer_2.setDisable(false);
    answer_3.setDisable(false);
    setButtons();
    showPoints();
  }

  public void setButtons() {
    Random random = new Random();
    int place = random.nextInt(3);
    switch (place) {
      case 1:
        answer_1.setText(activity.getConsumption_in_wh() + "Wh");
        answer_2.setText(question.getWrong1() + "Wh");
        answer_3.setText(question.getWrong2() + "Wh");
        buttons[0] = true;
        break;
      case 2:
        answer_2.setText(activity.getConsumption_in_wh() + "Wh");
        answer_1.setText(question.getWrong1() + "Wh");
        answer_3.setText(question.getWrong2() + "Wh");
        buttons[1] = true;
        break;
      default:
        answer_3.setText(activity.getConsumption_in_wh() + "Wh");
        answer_1.setText(question.getWrong1() + "Wh");
        answer_2.setText(question.getWrong2() + "Wh");
        buttons[2] = true;
        break;
    }
  }

  public void choose1() {
    answer_1.getStyleClass().add("chosen");
    answer_2.setDisable(true);
    answer_3.setDisable(true);
    chosen = 1;
    checkAnswer();
  }

  public void choose2() {
    answer_2.getStyleClass().add("chosen");
    answer_1.setDisable(true);
    answer_3.setDisable(true);
    chosen = 2;
    checkAnswer();
  }

  public void choose3() {
    answer_3.getStyleClass().add("chosen");
    answer_2.setDisable(true);
    answer_1.setDisable(true);
    chosen = 3;
    checkAnswer();
  }

  public void checkAnswer() {
    if (buttons[chosen - 1]) {
      mainCtrl.addPoints(100);
      showPoints();
    }
  }

  /**
   * Displays user points at the start of the question
   */
  public void showPoints() {
    int userPoints = mainCtrl.getPoints();
    points.setText("Points: " + userPoints);
  }

}
