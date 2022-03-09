/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.utils.ServerUtils;
import commons.Activity;
import commons.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.inject.Inject;

public class MainCtrl {

  private final ServerUtils server;

  private Stage primaryStage;

  private SplashCtrl splashCtrl;
  private Parent splashParent;

  private ConnectScreenCtrl connectCtrl;
  private Parent connectParent;

  private WaitingRoomCtrl waitingRoomCtrl;
  private Parent waitingRoomParent;

  private HowMuchCtrl howMuchCtrl;
  private Parent howMuchParent;

  private WhatRequiresMoreEnergyCtrl whatRequiresMoreEnergyCtrl;
  private Parent whatRequiresMoreEnergyParent;

  private GuessCtrl guessCtrl;
  private Parent guessParent;

  //if false, the player plays in singleplayer mode
  // if true, the player plays in multiplayer mode
  public boolean multiplayer;


  private ActivityListCtrl activityListCtrl;
  private Parent activityListParent;

  private EditActivityCtrl editActivityCtrl;
  private Parent editActivityParent;

  private HelpOverlayCtrl helpOverlayCtrl;
  private Parent helpOverlayParent;

  private ExitOverlayCtrl exitOverlayCtrl;
  private Parent exitOverlayParent;

  public String clientId;

  @Inject
  public MainCtrl(ServerUtils server) {
    this.server = server;
  }

  public boolean isMultiplayer() {
    return multiplayer;
  }

  public enum Mode {
    MULTI(0),
    SINGLE(1),
    ADMIN(2);

    private final int mode;

    private Mode(int m) {
      mode = m;
    }
  }

  public Mode mode;


  /**
   * The user's name in the current game.
   * Null if not in a game.
   */
  public String name = null;

  public void initialize(
    Stage primaryStage,
    Pair<SplashCtrl, Parent> splash,
    Pair<ConnectScreenCtrl, Parent> connect,
    Pair<WaitingRoomCtrl, Parent> waitingRoom,
    Pair<HowMuchCtrl, Parent> howMuch,
    Pair<WhatRequiresMoreEnergyCtrl, Parent> whatRequiresMoreEnergy,
    Pair<GuessCtrl, Parent> guess,
    Pair<ActivityListCtrl, Parent> activityList,
    Pair<EditActivityCtrl, Parent> editActivity,
    Pair<HelpOverlayCtrl, Parent> helpOverlay,
    Pair<ExitOverlayCtrl, Parent> exitOverlay
  ) {

    this.primaryStage = primaryStage;

    this.connectCtrl = connect.getKey();
    this.connectParent = connect.getValue();

    this.splashCtrl = splash.getKey();
    this.splashParent = splash.getValue();

    this.waitingRoomCtrl = waitingRoom.getKey();
    this.waitingRoomParent = waitingRoom.getValue();

    this.howMuchCtrl = howMuch.getKey();
    this.howMuchParent = howMuch.getValue();

    this.whatRequiresMoreEnergyCtrl = whatRequiresMoreEnergy.getKey();
    this.whatRequiresMoreEnergyParent = whatRequiresMoreEnergy.getValue();

    this.guessCtrl = guess.getKey();
    this.guessParent = guess.getValue();

    this.activityListCtrl = activityList.getKey();
    this.activityListParent = activityList.getValue();

    this.editActivityCtrl = editActivity.getKey();
    this.editActivityParent = editActivity.getValue();

    this.helpOverlayCtrl = helpOverlay.getKey();
    this.helpOverlayParent = helpOverlay.getValue();

    this.exitOverlayCtrl = exitOverlay.getKey();
    this.exitOverlayParent = exitOverlay.getValue();

    primaryStage.setTitle("Quizzzzz");
    // never exit full screen
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

    // set initial scene (splash) and show
    primaryStage.setScene(new Scene(connectParent));
    primaryStage.show();
    primaryStage.setFullScreen(true);
  }

  @FXML
  public void exit() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  public void backToMenu() {
    showSplash();
  }

  // instead of swapping entire scene, just swap parent
  public void showSplash() {
    // reset name and list of players if coming out of a game
    name = null;
    //players = null;
    primaryStage.getScene().setRoot(splashParent);
  }

  public void showConnect() {
    // reset name and list of players if coming out of a game
    name = null;
    //players = null;
    primaryStage.getScene().setRoot(connectParent);
  }

  public void showHowMuch() {
    primaryStage.getScene().setRoot(howMuchParent);
    howMuchCtrl.startTimer();
  }

  public void showWaitingRoom() {
    primaryStage.getScene().setRoot(waitingRoomParent);
    waitingRoomCtrl.connect();
    waitingRoomCtrl.refresh();
  }

  public void start() {
    server.startGame();
  }

  public void play() throws InterruptedException {
    nextQuestion();
  }

  // TODO: Long polling
  private void nextQuestion() throws InterruptedException {
    Question question = server.nextQuestion();
    if (question == null) {
      //TODO: Show end screen
    } else {
      switch (question.type) {
        case MULTICHOICE:
          showWhatRequiresMoreEnergy();
          break;

        case ESTIMATE:
          showGuess();
          break;
        case HOWMUCH:
          showHowMuch();
          break;
        default:
          //TODO do something if it doesn't work
          break;
      }
    }

  }

  public void showGuess() {
    primaryStage.getScene().setRoot(guessParent);
    guessCtrl.startTimer();
  }

  public void showWhatRequiresMoreEnergy() {
    primaryStage.getScene().setRoot(whatRequiresMoreEnergyParent);
    whatRequiresMoreEnergyCtrl.startTimer();
  }

  public void showActivityList() {
    // reset name and list of players if coming out of a game
    primaryStage.getScene().setRoot(activityListParent);
    activityListCtrl.refresh();
  }

  public void showEditActivity(Activity activity) {
    // reset name and list of players if coming out of a game
    primaryStage.getScene().setRoot(editActivityParent);
    editActivityCtrl.refresh(activity);
  }

  public void openHelp() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(helpOverlayParent);
  }

  public void closeHelp() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(helpOverlayParent);
  }

  public void openExitOverlay(boolean closeApp) {
    exitOverlayCtrl.closeApp = closeApp;
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(exitOverlayParent);
  }

  public void closeExitOverlay() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(exitOverlayParent);
  }
}
