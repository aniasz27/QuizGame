package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import java.net.URL;
import java.util.Arrays;
import java.util.OptionalLong;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class WhatRequiresMoreEnergyCtrl implements Initializable {

  @FXML
  private Button backButton;
  @FXML
  private Button button0;
  @FXML
  private Button button1;
  @FXML
  private Button button2;

  ServerUtils server;
  MainCtrl mainCtrl;

  Button[] buttons;
  Activity[] activities;

  @Inject
  WhatRequiresMoreEnergyCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }


  @FXML
  private void back(ActionEvent actionEvent) {
    mainCtrl.showSplash();
  }

  @FXML
  private void checkCorrectAnswer(ActionEvent actionEvent) {
    Button clickedButton = (Button) actionEvent.getSource();

    if ((boolean) clickedButton.getUserData()) {
      showUserCorrect();
    } else {
      showUserIncorrect();
    }
    ;
    for (Button button : buttons) {
      showButtonCorrectness(button);
    }
  }

  private void showUserCorrect() {
    //TODO: Add points to user and show prompt.
  }

  private void showUserIncorrect() {
    //TODO: Give no points to user and show prompt.
  }

  /**
   * Sets button color to appropriate given correctness of answer
   *
   * @param button button to assign color
   */
  private void showButtonCorrectness(Button button) {
    // set color to green (#2dff26) if answer was correct,
    // set it to red (#ff1717) otherwise
    String style = "-fx-background-color: "
      + (((boolean) button.getUserData()) ? "#2dff26" : "#ff1717")
      + ";";

    button.setStyle(style);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO: get 3 random activities from endpoint
    // activities = <Activity[] array here>

    buttons = new Button[] {button0, button1, button2};

    //OptionalLong lowestConsumptionInWh = Arrays.stream(activities).mapToLong(
    //  Activity::getConsumptionInWh).min();

    //TODO: change i < 0 -> i < buttons.length when activities can be get
    for (int i = 0; i < 0; i++) {
      Activity activity = activities[i];

      // get image
      ImageView imageView =
        new ImageView(getClass()
          .getResource(activity.getImage_path()).toExternalForm());
      // resize image
      imageView.setFitWidth(1140 / 3.0);
      imageView.setFitHeight(1140 / 3.0);

      //set image
      buttons[i].setGraphic(imageView);

      // image is displayed on top of text
      buttons[i].setContentDisplay(ContentDisplay.TOP);


      buttons[i].setText(activity.getTitle());
      // TODO: buttons[i].setUserData(), put if answer is correct or not
      // buttons[i].setUserData();
    }
  }
}
