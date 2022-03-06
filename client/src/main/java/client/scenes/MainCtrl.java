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
import commons.Question;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

  private final ServerUtils server;
  private Stage primaryStage;

  private SplashCtrl splashCtrl;
  private Parent splashParent;

  private ConnectScreenCtrl connectCtrl;
  private Parent connectParent;

  private WaitingRoomCtrl waitingRoomCtrl;
  private Parent waitingRoomParent;

  //if false, the player plays in singleplayer mode
  // if true, the player plays in multiplayer mode
  private boolean multiplayer;

  public MainCtrl(ServerUtils server) {
    this.server = server;
  }

  public boolean isMultiplayer() {
    return multiplayer;
  }

  public void setMultiplayer(boolean multiplayer) {
    this.multiplayer = multiplayer;
  }


  public void initialize(
    Stage primaryStage,
    Pair<SplashCtrl, Parent> splash,
    Pair<ConnectScreenCtrl, Parent> connect,
    Pair<WaitingRoomCtrl, Parent> waitingRoom
  ) {

    this.primaryStage = primaryStage;

    this.splashCtrl = splash.getKey();
    this.splashParent = splash.getValue();

    this.connectCtrl = connect.getKey();
    this.connectParent = connect.getValue();

    this.waitingRoomCtrl = waitingRoom.getKey();
    this.waitingRoomParent = waitingRoom.getValue();

    primaryStage.setTitle("Quizzzzz");
    // never exit full screen
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

    // set initial scene (splash) and show
    primaryStage.setScene(new Scene(splashParent));
    primaryStage.show();
    primaryStage.setFullScreen(true);
  }

  // instead of swapping entire scene, just swap parent
  public void showSplash() {
    primaryStage.getScene().setRoot(splashParent);
  }

  public void showConnect() {
    primaryStage.getScene().setRoot(connectParent);
    /* TODO */
  }

  public void showWaitingRoom() {
    primaryStage.getScene().setRoot(waitingRoomParent);
    waitingRoomCtrl.connect();
    waitingRoomCtrl.refresh();
  }

  public void play() {
    Question question = server.nextQuestion();
    if (question == null) {
      //TODO: Show end screen
    } else {
      switch (question.type) {
        case MULTICHOICE:
          //TODO show multiple choice screen
          break;

        case ESTIMATE:
          //TODO show estimate screen
          break;

        default:
          //TODO do something if it doesn't work
          break;
      }
    }
  }
}
