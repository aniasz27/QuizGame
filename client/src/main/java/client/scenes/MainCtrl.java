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

import java.awt.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

  private Stage primaryStage;

  private QuoteOverviewCtrl overviewCtrl;
  private Scene overview;

  private AddQuoteCtrl addCtrl;
  private Scene add;
  private ConnectScreenCtrl connectCtrl;
  private Scene connect;

  //if false, the player plays in singleplayer mode
  // if true, the player plays in multiplayer mode
  private boolean multi;

  @FXML
  private Button buttonMulti;

  @FXML
  private Button buttonSingle;

  @FXML
  public void multiplayerFalse() {
    this.multi = false;
    showConnect();
  }

  @FXML
  public void multiplayerTrue() {
    this.multi = true;
    showConnect();
  }


  public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                         Pair<AddQuoteCtrl, Parent> add, Pair<ConnectScreenCtrl, Parent> connect) {
    this.primaryStage = primaryStage;

    // TODO: remove addCtrl and overviewCtrl
    this.overviewCtrl = overview.getKey();
    this.overview = new Scene(overview.getValue());

    this.addCtrl = add.getKey();
    this.add = new Scene(add.getValue());

    this.connectCtrl = connect.getKey();
    this.connect = new Scene(connect.getValue());

    // TODO: Show splash screen first.
    showOverview();
    primaryStage.show();
  }

  public void showSplash() {
    primaryStage.setTitle("Quizzzz");
    primaryStage.setScene(overview);
    overviewCtrl.refresh();
  }

  public void showOverview() {
    primaryStage.setTitle("Quotes: Overview");
    primaryStage.setScene(overview);
    overviewCtrl.refresh();
  }

  public void showAdd() {
    primaryStage.setTitle("Quotes: Adding Quote");
    primaryStage.setScene(add);
    add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
  }

  private void showConnect() {
    primaryStage.setTitle("Connect");
    primaryStage.setScene(add);
    /* TODO */
  }

  public void showWaiting() {
    // TODO: Show the waiting screen
  }
}
