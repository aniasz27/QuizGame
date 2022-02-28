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

package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.ConnectScreenCtrl;
import client.scenes.MainCtrl;
import client.scenes.SplashCtrl;
import client.scenes.WaitingRoomCtrl;
import com.google.inject.Injector;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  private static final Injector INJECTOR = createInjector(new MyModule());
  private static final MyFXML FXML = new MyFXML(INJECTOR);

  public static void main(String[] args) throws URISyntaxException, IOException {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    var splash = FXML.load(
      SplashCtrl.class,
      "client/scenes/Splash.fxml",
      null
    );
    var connectScreen = FXML.load(
      ConnectScreenCtrl.class,
      "client/scenes/ConnectScreen.fxml",
      null
    );
    var waitingRoom = FXML.load(
      WaitingRoomCtrl.class,
      "client/scenes/WaitingRoom.fxml",
      "client/css/WaitingRoom.css"
    );

    var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
    mainCtrl.initialize(primaryStage, splash, connectScreen, waitingRoom);
  }

  //The real application
  /* @Override
  public void start(Stage primaryStage) throws IOException {
    var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "Splash.fxml");
    var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
    // to be replaced with the connect screen
    var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
    mainCtrl.initialize(primaryStage, overview, add);
    //connects client to the server for the first time
    overview.getKey().connect();
  }*/
}