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

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

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

  /**
   * Map of all players and their scores in the current game
   * Null if not in a game
   */
  public Map<String, Integer> players = null;

  /**
   * The user's name in the current game.
   * Null if not in a game
   */
  public String name = null;

  public void initialize(
    Stage primaryStage,
    Pair<SplashCtrl, Parent> splash,
    Pair<ConnectScreenCtrl, Parent> connect,
    Pair<WaitingRoomCtrl, Parent> waitingRoom,
    Pair<HowMuchCtrl, Parent> howMuch,
    Pair<WhatRequiresMoreEnergyCtrl, Parent> whatRequiresMoreEnergy,
    Pair<GuessCtrl, Parent> guess
  ) {
    this.primaryStage = primaryStage;

    this.splashCtrl = splash.getKey();
    this.splashParent = splash.getValue();

    this.connectCtrl = connect.getKey();
    this.connectParent = connect.getValue();

    this.waitingRoomCtrl = waitingRoom.getKey();
    this.waitingRoomParent = waitingRoom.getValue();

    this.howMuchCtrl = howMuch.getKey();
    this.howMuchParent = howMuch.getValue();

    this.whatRequiresMoreEnergyCtrl = whatRequiresMoreEnergy.getKey();
    this.whatRequiresMoreEnergyParent = whatRequiresMoreEnergy.getValue();

    this.guessCtrl = guess.getKey();
    this.guessParent = guess.getValue();

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
    if (alert()) {
      Platform.exit();
      System.exit(0);
    }
  }

  public boolean alert() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Exit");
    alert.setHeaderText("You are about to leave the game");
    alert.setContentText("Are you sure?");
    return alert.showAndWait().get() == ButtonType.OK;
  }

  @FXML
  public void goBackToMenu() {
    if (alert()) {
      showSplash();
    }
  }

  // instead of swapping entire scene, just swap parent
  public void showSplash() {
    // reset name and list of players if coming out of a game
    name = null;
    players = null;
    primaryStage.getScene().setRoot(splashParent);
  }

  public void showConnect() {
    // reset name and list of players if coming out of a game
    name = null;
    players = null;
    primaryStage.getScene().setRoot(connectParent);
  }

  public void showHowMuch() {
    primaryStage.getScene().setRoot(howMuchParent);
  }

  public void showWaitingRoom() {
    primaryStage.getScene().setRoot(waitingRoomParent);
    // TODO: replace with getting players from the server
    name = "Player 1";
    players = new LinkedHashMap<>();
    players.put(name, 0);
    players.put("Nikola Tesla", 0);
    players.put("Player 2", 0);
    players.put("Player 3", 0);
    players.put("Player 4", 0);
    players.put("Player 5", 0);
    players.put("James Watt", 0);
    players.put("Player 6", 0);
    players.put("Thomas Edison", 0);
    players.put("Player 7", 0);
    players.put("Player 8", 0);
    players.put("Player 9", 0);
    players.put("Player 10", 0);
    players.put("Player 11", 0);
    players.put("Player 12", 0);
    players.put("Player 13", 0);
    players.put("Player 14", 0);
    players.put("Player 15", 0);
    // ---------------------------------------------------
    waitingRoomCtrl.refresh(players);
  }

  public void play() {
    // TODO
  }

  public void showGuess() throws InterruptedException {
    primaryStage.getScene().setRoot(guessParent);
    guessCtrl.start();
  }
}
