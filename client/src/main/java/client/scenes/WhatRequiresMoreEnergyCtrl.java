package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.MultipleChoiceQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class WhatRequiresMoreEnergyCtrl extends QuestionCtrl implements Initializable {

  @FXML
  private Button button0;
  @FXML
  private Button button1;
  @FXML
  private Button button2;
  @FXML
  private Text points;

  Button[] buttons;
  MultipleChoiceQuestion question;

  @Inject
  WhatRequiresMoreEnergyCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @FXML
  public void checkCorrectAnswer(MouseEvent event) {
    Button clickedButton = (Button) event.getSource();
    if (clickedButton.getUserData() == null) {
      return;
    }

    if ((boolean) clickedButton.getUserData()) {
      showUserCorrect();
    } else {
      showUserIncorrect();
    }

    for (Button button : buttons) {
      showButtonCorrectness(button);
    }
  }


  /**
   * Displays user points at the start of the question
   */
  public void showPoints() {
    int userPoints = mainCtrl.getPoints();
    points.setText("Points: " + userPoints);
  }

  private void showUserIncorrect() {
    //TODO: Give no points to user and show prompt.
  }


  /**
   * Sets button color to appropriate given correctness of answer
   *
   * @param button button to assign color
   */
  public void showButtonCorrectness(Button button) {
    if (button.getUserData() == null) {
      return;
    }

    button.getStyleClass().add((boolean) button.getUserData() ? "good" : "bad");
  }


  public void showUserCorrect() {
    mainCtrl.addPoints(100);
    showPoints();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttons = new Button[] {button0, button1, button2};
  }

  @Override
  public void displayQuestion(Question question) {
    MultipleChoiceQuestion castedQuestion = (MultipleChoiceQuestion) question;

    // reset correct button colors
    for (Button button : buttons) {
      button.getStyleClass().remove("good");
      button.getStyleClass().remove("bad");
    }

    Activity[] activities = {
      castedQuestion.getActivity1(),
      castedQuestion.getActivity2(),
      castedQuestion.getActivity3()
    };

    boolean[] correctAnswers = castedQuestion.getCorrect();

    //TODO: change i < 0 -> i < buttons.length when activities can be get
    for (int i = 0; i < buttons.length; i++) {
      Activity activity = activities[i];

      String path = "/client/JSON/" + activity.getImage_path();

      // get image
      ImageView imageView = new ImageView(new Image(
        new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))
      ));

      // resize image
      imageView.setFitWidth(1140 / 3.0);
      imageView.setFitHeight(1140 / 3.0);

      //set image
      buttons[i].setGraphic(imageView);

      // image is displayed on top of text
      buttons[i].setContentDisplay(ContentDisplay.TOP);

      buttons[i].setText(activity.getTitle());
      // TODO: buttons[i].setUserData(), put if answer is correct or not
      buttons[i].setUserData(correctAnswers[i]);
    }
  }
}
