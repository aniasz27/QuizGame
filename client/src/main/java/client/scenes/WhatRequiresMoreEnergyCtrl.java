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

  Button[] buttons = {button0, button1, button2};
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
      if ((boolean) button.getUserData()) {
        showButtonCorrect(button);
      } else {
        showButtonIncorrect(button);
      }
    }
  }

  private void showUserCorrect() {
    //TODO: Add points to user and show prompt.
  }

  private void showUserIncorrect() {
    //TODO: Give no points to user and show prompt.
  }

  /**
   * Sets button color to red, to show that the answer was incorrect.
   *
   * @param button button to show correctness of.
   */
  private void showButtonCorrect(Button button) {
    button.setStyle("-fx-background-color: #ff1717;");
  }

  /**
   * Sets button color to green, to show that the answer was correct.
   *
   * @param button button to show correctness of.
   */
  private void showButtonIncorrect(Button button) {
    button.setStyle("-fx-background-color: #2dff26;");
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //TODO: get 3 random activities from endpoint

    OptionalLong lowestConsumptionInWh = Arrays.stream(activities).mapToLong(
      Activity::getConsumptionInWh).min();

    for (int i = 0; i < buttons.length; i++) {
      Activity activity = activities[i];

      ImageView imageView =
        new ImageView(getClass()
          .getResource(activity.getImagePath()).toExternalForm());
      buttons[i].setGraphic(imageView);

      buttons[i].setText(activity.getTitle());
      buttons[i].setUserData(
        lowestConsumptionInWh.equals(activity.getConsumptionInWh())
      );
    }
  }
}
