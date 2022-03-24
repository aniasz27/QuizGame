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
import commons.EstimateQuestion;
import commons.HowMuchQuestion;
import commons.MultipleChoiceQuestion;
import commons.Question;
import commons.Score;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
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

  private SpWaitingRoomCtrl spWaitingRoomCtrl;
  private Parent spWaitingRoomParent;

  private HowMuchCtrl howMuchCtrl;
  private Parent howMuchParent;

  private WhatRequiresMoreEnergyCtrl whatRequiresMoreEnergyCtrl;
  private Parent whatRequiresMoreEnergyParent;

  private GuessCtrl guessCtrl;
  private Parent guessParent;

  private IntermediateLeaderboardCtrl intermediateLeaderboardCtrl;
  private Parent intermediateLeaderboardParent;

  private ActivityListCtrl activityListCtrl;
  private Parent activityListParent;

  private EditActivityCtrl editActivityCtrl;
  private Parent editActivityParent;

  private HelpOverlayCtrl helpOverlayCtrl;
  private Parent helpOverlayParent;

  private ExitOverlayCtrl exitOverlayCtrl;
  private Parent exitOverlayParent;

  private EndScreenCtrl endScreenCtrl;
  private Parent endScreenParent;

  //if false, the player plays in singleplayer mode
  // if true, the player plays in multiplayer mode
  public boolean multiplayer;
  public String serverIp;
  public String clientId;
  public String gameId;
  public ScheduledExecutorService keepAliveExec;
  public boolean waitingForGame;
  public boolean[] usedJokers;
  public int questionNumber = -1;

  private int points;

  private Question question;
  public Thread timerThread;
  public boolean playerExited = false;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
  private Date pointsTimer;
  private int pointsOffset;

  @Inject
  public MainCtrl(ServerUtils server) {
    this.server = server;
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
    Pair<SpWaitingRoomCtrl, Parent> spWaitingRoom,
    Pair<HowMuchCtrl, Parent> howMuch,
    Pair<WhatRequiresMoreEnergyCtrl, Parent> whatRequiresMoreEnergy,
    Pair<GuessCtrl, Parent> guess,
    Pair<IntermediateLeaderboardCtrl, Parent> intermediateLeaderboard,
    Pair<ActivityListCtrl, Parent> activityList,
    Pair<EditActivityCtrl, Parent> editActivity,
    Pair<HelpOverlayCtrl, Parent> helpOverlay,
    Pair<ExitOverlayCtrl, Parent> exitOverlay,
    Pair<EndScreenCtrl, Parent> endScreen
  ) {
    this.primaryStage = primaryStage;
    this.connectCtrl = connect.getKey();
    this.connectParent = connect.getValue();

    this.splashCtrl = splash.getKey();
    this.splashParent = splash.getValue();

    this.waitingRoomCtrl = waitingRoom.getKey();
    this.waitingRoomParent = waitingRoom.getValue();

    this.spWaitingRoomCtrl = spWaitingRoom.getKey();
    this.spWaitingRoomParent = spWaitingRoom.getValue();

    this.howMuchCtrl = howMuch.getKey();
    this.howMuchParent = howMuch.getValue();

    this.whatRequiresMoreEnergyCtrl = whatRequiresMoreEnergy.getKey();
    this.whatRequiresMoreEnergyParent = whatRequiresMoreEnergy.getValue();

    this.guessCtrl = guess.getKey();
    this.guessParent = guess.getValue();

    this.intermediateLeaderboardCtrl = intermediateLeaderboard.getKey();
    this.intermediateLeaderboardParent = intermediateLeaderboard.getValue();

    this.activityListCtrl = activityList.getKey();
    this.activityListParent = activityList.getValue();

    this.editActivityCtrl = editActivity.getKey();
    this.editActivityParent = editActivity.getValue();

    this.helpOverlayCtrl = helpOverlay.getKey();
    this.helpOverlayParent = helpOverlay.getValue();

    this.exitOverlayCtrl = exitOverlay.getKey();
    this.exitOverlayParent = exitOverlay.getValue();

    this.endScreenCtrl = endScreen.getKey();
    this.endScreenParent = endScreen.getValue();

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
    this.points = 0;
    //players = null;
    primaryStage.getScene().setRoot(splashParent);
  }

  public void showConnect() {
    // reset name and list of players if coming out of a game
    name = null;
    //players = null;

    primaryStage.getScene().setRoot(connectParent);
  }

  public void showWaitingRoom() {
    primaryStage.getScene().setRoot(waitingRoomParent);
    waitingRoomCtrl.refresh();
    waitingForGame = true;
    waitingRoomCtrl.listenForNewPlayers();
  }

  public void showSpWaitingRoom() {
    primaryStage.getScene().setRoot(spWaitingRoomParent);
    spWaitingRoomCtrl.refresh();
  }

  /**
   * Starts the game, assigns the points from the game controller
   */
  public void start() {
    this.usedJokers = new boolean[3];
    gameId = server.startGame(serverIp);
    points = 0;
    play();
  }

  public void play() {
    playerExited = false;
    nextRound();
  }

  /**
   * Returns the points the player has acquired so far
   */
  public int getPoints() {
    return points;
  }

  /**
   * Shows the next question, starts a timer from the server and uses long polling to determine when to change state
   */
  public void nextRound() {
    if (playerExited) {
      return;
    }

    nextQuestion();

    Task<Void> task = new Task<>() {
      @Override
      protected Void call() {
        // do not start timer for next question if on end screen
        if (!question.type.equals(Question.Type.ENDSCREEN)) {
          startQuestionTimer();
        }

        return null;
      }
    };

    timerThread = new Thread(task);
    timerThread.setDaemon(true);
    timerThread.start();
  }

  public void startQuestionTimer() {
    // set a timer for 10s (question duration)
    boolean finished = server.startServerTimer(serverIp, clientId, 10000);

    if (finished) {
      switch (question.type) {
        case MULTICHOICE:
          Platform.runLater(() -> whatRequiresMoreEnergyCtrl.disableButtons());
          Platform.runLater(() -> whatRequiresMoreEnergyCtrl.showCorrect());
          break;
        case ESTIMATE:
          Platform.runLater(() -> guessCtrl.disableButtons());
          Platform.runLater(() -> guessCtrl.showCorrect());
          break;
        case HOWMUCH:
          Platform.runLater(() -> howMuchCtrl.disableButtons());
          Platform.runLater(() -> howMuchCtrl.showCorrect());
          break;
        default:
          System.out.println("Not a question");
          break;
      }
      startBreakTimer();
    } else {
      System.err.println("Error in question timer.");
    }
  }

  public void startBreakTimer() {
    boolean finished = server.startServerTimer(serverIp, clientId, 2000); // 2s time given for break

    if (finished) {
      nextRound();
    } else {
      System.err.println("Error in break timer.");
    }
  }

  private void nextQuestion() {
    question = server.nextQuestion(serverIp, gameId, questionNumber);
    questionNumber = question.number;
    switch (question.type) {
      case MULTICHOICE:
        System.out.println("Showed multiple choice");
        Platform.runLater(() -> showWhatRequiresMoreEnergy((MultipleChoiceQuestion) question));
        break;
      case ESTIMATE:
        System.out.println("Showed guess");
        Platform.runLater(() -> showGuess((EstimateQuestion) question));
        break;
      case HOWMUCH:
        System.out.println("Showed how much");
        Platform.runLater(() -> showHowMuch((HowMuchQuestion) question));
        break;
      case INTERLEADERBOARD:
        System.out.println("Showed Intermediate Leaderboard");
        Platform.runLater(this::showIntermediateLeaderboard);
        break;
      case ENDSCREEN:
        System.out.println("Showed end screen");
        server.addScore(serverIp, new Score(clientId, name, points));
        Platform.runLater(this::showEndScreen);
        break;
      default:
        System.out.println("Wrong question type");
        break;
    }
  }

  public void showGuess(EstimateQuestion question) {
    primaryStage.getScene().setRoot(guessParent);
    guessCtrl.displayQuestion(question);
    guessCtrl.startTimer();
    this.startPointsTimer();
  }

  public void showWhatRequiresMoreEnergy(MultipleChoiceQuestion question) {
    primaryStage.getScene().setRoot(whatRequiresMoreEnergyParent);
    whatRequiresMoreEnergyCtrl.displayQuestion(question);
    whatRequiresMoreEnergyCtrl.startTimer();
    this.startPointsTimer();
  }

  public void showHowMuch(HowMuchQuestion question) {
    primaryStage.getScene().setRoot(howMuchParent);
    howMuchCtrl.displayQuestion(question);
    howMuchCtrl.startTimer();
    this.startPointsTimer();
  }

  public void showIntermediateLeaderboard() {
    primaryStage.getScene().setRoot(intermediateLeaderboardParent);
    intermediateLeaderboardCtrl.display();
    intermediateLeaderboardCtrl.refresh();
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


  /**
   * Shows the end screen to the user, updates the user points in the game controller
   */
  public void showEndScreen() {
    primaryStage.getScene().setRoot(endScreenParent);
    endScreenCtrl.refresh();
    Score check = server.updateScore(serverIp, clientId, points);
    if (check == null) {
      System.out.println("Error updating the score");
    }
  }

  public void addPoints(int toAdd) {
    points += toAdd;
  }

  /**
   * Sets pointsTimer to the time the question was shown
   */
  public void startPointsTimer() {
    try {
      String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
      pointsTimer = simpleDateFormat.parse(timeNow);
    } catch (ParseException e) {
      pointsTimer = null;
    }
  }

  /**
   * Calculates the difference between the moment the question was shown and the moment the answer button was pressed
   * Saves the difference in pointsOffset
   */
  public void stopPointsTimer() {
    int dif = -1;
    if (pointsTimer != null) {
      try {
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS"));
        dif = (int) (simpleDateFormat.parse(timeNow).getTime() - pointsTimer.getTime()) / 100 % 100;
      } catch (ParseException e) {
        System.out.println("Error when calculating time difference");
      }
    } else {
      System.out.println("Error when calculating time difference");
    }
    pointsTimer = null;
    this.pointsOffset = 100 - dif;
  }

  /**
   * Returns the offset, but if it's larger than 75, it gives full points (offset is 100)
   *
   * @return a value between 0-100
   */
  public int getPointsOffset() {
    if (pointsOffset >= 75) {
      return 100;
    }
    return Math.max(pointsOffset, 0);
  }

  /**
   * Leaderboard creator method. Will display:
   * - playerName
   * - visual representation of the progress between player
   * - points
   *
   * @param leaderboardDisplay VBox which will include the leaderboard
   * @param scores             Information
   */
  public static void refreshLeaderboard(VBox leaderboardDisplay, Iterable<Score> scores) {
    leaderboardDisplay.getChildren().removeAll(leaderboardDisplay.getChildren());

    final boolean[] first = {true};

    AtomicInteger maxScore;
    maxScore = new AtomicInteger();
    AtomicInteger rowCounter = new AtomicInteger();
    rowCounter.set(0);
    GridPane gridpane = new GridPane();
    gridpane.setMaxWidth(640);
    gridpane.setAlignment(Pos.CENTER);
    ColumnConstraints name = new ColumnConstraints(290);
    ColumnConstraints bar = new ColumnConstraints(240);
    ColumnConstraints points = new ColumnConstraints(100);
    name.setHgrow(Priority.SOMETIMES);
    bar.setHgrow(Priority.SOMETIMES);
    points.setHgrow(Priority.SOMETIMES);
    gridpane.getColumnConstraints().addAll(name, bar, points);

    leaderboardDisplay.getChildren().removeAll();
    scores.forEach(s -> {
      Label label = new Label();
      label.getStyleClass().add("expand");
      label.getStyleClass().add("list-item");
      label.getStyleClass().add("border-bottom");
      label.setText(s.getName());
      if (first[0]) {
        label.getStyleClass().add("list-item-top-left");
      }
      gridpane.add(label, 0, rowCounter.get());

      StackPane stackPane = new StackPane();
      stackPane.setAlignment(Pos.CENTER_LEFT);
      stackPane.getStyleClass().add("list-item");
      stackPane.getStyleClass().add("border-bottom");
      Line line = new Line();
      line.setEndX(200);
      line.setStrokeLineCap(StrokeLineCap.ROUND);
      line.setStrokeWidth(20);
      line.setStroke(Paint.valueOf("#553794"));

      stackPane.getChildren().add(line);
      line = new Line();
      line.setStrokeLineCap(StrokeLineCap.ROUND);
      line.setStrokeWidth(20);
      line.setStroke(Paint.valueOf("#0586e3"));
      if (first[0]) {
        line.setEndX(200);
        maxScore.set(s.getPoints());

      } else {
        line.setEndX(200 * s.getPoints() / maxScore.get());
      }
      line.getStyleClass().add("timer-bar");

      stackPane.getChildren().add(line);

      gridpane.add(stackPane, 1, rowCounter.get());

      label = new Label();
      label.setText(String.valueOf(s.getPoints()));
      label.getStyleClass().add("expand");
      label.getStyleClass().add("list-item");
      label.getStyleClass().add("border-bottom");
      if (first[0]) {
        label.getStyleClass().add("list-item-top-right");
        first[0] = false;
      }
      gridpane.add(label, 2, rowCounter.get());
      rowCounter.getAndIncrement();
    });
    leaderboardDisplay.getChildren().add(gridpane);
  }


  public void reset() {
    serverIp = null;
    clientId = null;
    gameId = null;
    waitingForGame = false;
    questionNumber = -1;
    points = 0;
    question = null;
    keepAliveExec.shutdownNow();
    keepAliveExec = null;
  }
}
