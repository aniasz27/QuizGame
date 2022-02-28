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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

  private Stage primaryStage;

  private SplashCtrl splashCtrl;
  private Scene splash;

  private ConnectScreenCtrl connectCtrl;
  private Scene connect;

  private WaitingRoomCtrl waitingRoomCtrl;
  private Scene waitingRoom;

  //if false, the player plays in singleplayer mode
  // if true, the player plays in multiplayer mode
  private boolean multiplayer;

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
    this.splash = new Scene(splash.getValue());

    this.connectCtrl = connect.getKey();
    this.connect = new Scene(connect.getValue());

    this.waitingRoomCtrl = waitingRoom.getKey();
    this.waitingRoom = new Scene(waitingRoom.getValue());

    primaryStage.setTitle("Quizzzzz");

    showSplash();
    primaryStage.show();
    primaryStage.setFullScreen(true);
  }

  public void showSplash() {
    primaryStage.setScene(splash);
    primaryStage.setFullScreen(true);
  }

  public void showConnect() {
    primaryStage.setScene(connect);
    primaryStage.setFullScreen(true);
    /* TODO */
  }

  public void showWaitingRoom() {
    primaryStage.setScene(waitingRoom);
    primaryStage.setFullScreen(true);
    waitingRoomCtrl.refresh();
  }
}
