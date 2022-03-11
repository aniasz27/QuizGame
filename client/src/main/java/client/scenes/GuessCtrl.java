package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import commons.Activity;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GuessCtrl extends QuestionCtrl implements Initializable {

  @FXML
  private ImageView imageView;

  @FXML
  private Image image;

  @FXML
  private TextField answer;

  @FXML
  private Button submit;

  private int correctAnswer = 1;


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