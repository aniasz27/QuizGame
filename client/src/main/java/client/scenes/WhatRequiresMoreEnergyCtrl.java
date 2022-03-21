package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.MultipleChoiceQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
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

  private Button[] buttons;
  private Label[] emojis;
  private Button[] jokers;

  private Button clickedButton;
  private MultipleChoiceQuestion question;
  private boolean dbPoint;

  @Inject
  WhatRequiresMoreEnergyCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
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
   * Displays user points at the start of the question
   */
  public void showPoints() {
    points.setText("Points: " + mainCtrl.getPoints());
  }

  public void showUserCorrect() {
    int toAdd = mainCtrl.getPointsOffset();
    if (dbPoint) {
      toAdd *= 2;
    }
    mainCtrl.addPoints(toAdd);
    showPoints();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttons = new Button[] {button0, button1, button2};
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
  }

  @Override
  public void displayQuestion(Question question) {
    pane.setVisible(false);
    circle.setVisible(false);
    emojiGrid.setVisible(false);
    this.question = (MultipleChoiceQuestion) question;
    this.clickedButton = null;

    // reset correct button colors
    for (Button button : buttons) {
      button.getStyleClass().remove("good");
      button.getStyleClass().remove("bad");
      button.setDisable(false);
    }

    Activity[] activities = {
      this.question.getActivity1(),
      this.question.getActivity2(),
      this.question.getActivity3()
    };

    boolean[] correctAnswers = this.question.getCorrect();
    for (int i = 0; i < buttons.length; i++) {
      Activity activity = activities[i];

      // get image
      StackPane imgContainer = new StackPane();
      imgContainer.getStyleClass().add("rounded");
      imgContainer.getStyleClass().add("img");
      //Rectangle clip = new Rectangle(
      //  imgContainer.getWidth(), imgContainer.getHeight()
      //);
      //clip.setArcWidth(20);
      //clip.setArcHeight(20);
      //imgContainer.setClip(clip);

      ImageView imageView = new ImageView(new Image(
        new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))
      ));

      // resize image
      imageView.setFitWidth(1140 / 3.0);
      imageView.setFitHeight(1140 / 3.0);

      imgContainer.getChildren().add(imageView);

      //set image
      buttons[i].setGraphic(imgContainer);

      // image is displayed on top of text
      buttons[i].setContentDisplay(ContentDisplay.TOP);
      buttons[i].setText(activity.getTitle());
      buttons[i].setUserData(correctAnswers[i]);
    }
    for (Button joker : jokers) {
      joker.setDisable(false);
    }
  }

  /**
   * Sets button color to appropriate given correctness of answer
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
   * Disable buttons in case when user does not pick an answer
   */
  @Override
  public void disableButtons() {
    for (Button button : buttons) {
      button.setDisable(true);
    }
    for (Button joker : jokers) {
      joker.setDisable(true);
    }
  }

  public void hint() {
    if (hint.getStyleClass().contains("used")) {
      return;
    }
    Random random = new Random();
    int guess;
    do {
      guess = random.nextInt(3);
    } while (question.getCorrect()[guess]);
    buttons[guess].setDisable(true);
    hint.getStyleClass().add("used");
    hint.getStyleClass().remove("drop-shadow");
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
