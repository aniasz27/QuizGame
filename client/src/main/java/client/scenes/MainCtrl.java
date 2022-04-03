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

import client.scenes.helpers.QuestionCtrl;
import client.utils.EmojiWebSocket;
import client.utils.JokerWebSocket;
import client.utils.ServerUtils;
import commons.Activity;
import commons.Emoji;
import commons.EstimateQuestion;
import commons.HowMuchQuestion;
import commons.InsteadOfQuestion;
import commons.Joker;
import commons.MultipleChoiceQuestion;
import commons.Question;
import commons.Score;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

  public Stage primaryStage;

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

  private InsteadOfCtrl insteadOfCtrl;
  private Parent insteadOfParent;

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
  public int questionNumber = 0;
  private int points;
  private Question question;
  public boolean playerExited = false;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
  private Date pointsTimer;
  private int pointsOffset;
  public JokerWebSocket jokerWebSocket;
  public EmojiWebSocket emojiWebSocket;
  //The controller of the question that was last shown (ie currently being shown)
  public QuestionCtrl currentQuestionCtrl;
  //The user's name in the current game. Null if not in a game.
  public String name = null;

  /**
   * Constructor for the MainCtrl
   *
   * @param server we are on
   */
  @Inject
  public MainCtrl(ServerUtils server) {
    this.server = server;
  }

  /**
   * Initialising the MainCtrl
   *
   * @param primaryStage            primary stage
   * @param splash                  splash
   * @param connect                 connect
   * @param waitingRoom             waiting room
   * @param spWaitingRoom           spWaitingRoom
   * @param howMuch                 howMuch
   * @param insteadOf               insteadOf
   * @param whatRequiresMoreEnergy  whatRequiresMoreEnergy
   * @param guess                   guess
   * @param intermediateLeaderboard intermediateLeaderboard
   * @param activityList            activityList
   * @param editActivity            editActivity
   * @param helpOverlay             helpOverlay
   * @param exitOverlay             exitOverlay
   * @param endScreen               endScreen
   */
  public void initialize(
    Stage primaryStage,
    Pair<SplashCtrl, Parent> splash,
    Pair<ConnectScreenCtrl, Parent> connect,
    Pair<WaitingRoomCtrl, Parent> waitingRoom,
    Pair<SpWaitingRoomCtrl, Parent> spWaitingRoom,
    Pair<HowMuchCtrl, Parent> howMuch,
    Pair<InsteadOfCtrl, Parent> insteadOf,
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

    this.insteadOfCtrl = insteadOf.getKey();
    this.insteadOfParent = insteadOf.getValue();

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

    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon16.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon32.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon64.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon128.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon256.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon512.png")));
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/client/img/icon1024.png")));
    primaryStage.setTitle("Quizzzzz");
    // never exit full screen
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

    // set initial scene (splash) and show
    primaryStage.setScene(new Scene(connectParent));
    primaryStage.show();
    primaryStage.setFullScreen(true);
  }

  /**
   * Exists the game
   */
  @FXML
  public void exit() {
    Platform.exit();
    System.exit(0);
  }

  /**
   * Goes to SplashScreen
   */
  @FXML
  public void backToMenu() {
    showSplash();
  }

  /**
   * Shows the SplashScreen
   */
  public void showSplash() {
    // instead of swapping entire scene, just swap parent
    // reset name and list of players if coming out of a game
    this.points = 0;
    //players = null;
    primaryStage.getScene().setRoot(splashParent);
  }

  /**
   * Shows the ConnectScreen
   */

  public void showConnect() {
    // reset name and list of players if coming out of a game
    name = null;
    //players = null;

    connectCtrl.refresh();
    primaryStage.getScene().setRoot(connectParent);
  }

  /**
   * Shows the WaitingRoom
   */

  public void showWaitingRoom() {
    primaryStage.getScene().setRoot(waitingRoomParent);
    waitingRoomCtrl.refresh();
    waitingForGame = true;
    waitingRoomCtrl.listenForNewPlayers();
  }

  /**
   * Shows the SinglePlayerWaitingRoom
   */
  public void showSpWaitingRoom() {
    primaryStage.getScene().setRoot(spWaitingRoomParent);
    spWaitingRoomCtrl.refresh();
  }

  /**
   * Starts the game,initializes the gameid
   */
  public void start() {
    gameId = server.startGame(serverIp, this.multiplayer);
    play();
  }

  /**
   * Initializes the jokers, points, questionNumber and asks for nextQuestion
   */
  public void play() {
    this.usedJokers = new boolean[3];
    System.out.println("session: " + gameId);
    emojiWebSocket = new EmojiWebSocket(this, gameId);
    jokerWebSocket = new JokerWebSocket(this, gameId);
    playerExited = false;
    points = 0;
    questionNumber = 0;
    if (multiplayer) {
      server.stopUpdates();
    }
    nextQuestion();
  }

  /**
   * Returns the points the player has acquired so far
   */
  public int getPoints() {
    return points;
  }

  /**
   * Disables the buttons and shows the correct answers - question based
   */
  public void showAnswer() {
    switch (question.type) {
      case MULTICHOICE:
      case ESTIMATE:
      case HOWMUCH:
      case INSTEAD:
        Platform.runLater(() -> currentQuestionCtrl.disableButtons());
        Platform.runLater(() -> currentQuestionCtrl.showCorrect());
        break;
      case INTERLEADERBOARD:
      case ENDSCREEN:
        break;
      default:
        System.out.println("Not a question");
        break;
    }
  }

  /**
   * Gets the next Question from the server and calls a method to display it
   */
  private void nextQuestion() {
    server.nextQuestion(serverIp, gameId, question -> {
      if (!playerExited) {
        this.question = question;
        showCurrentQuestion();
      }
    });
  }

  /**
   * Shows the Question, which has been sent from the server
   */
  public void showCurrentQuestion() {
    System.out.println(question.number + " " + question.showCorrect + " " + question.type);
    if (question.showCorrect) {
      showAnswer();
    } else {
      switch (this.question.type) {
        case MULTICHOICE:
          System.out.println("Showed multiple choice");
          Platform.runLater(() -> showWhatRequiresMoreEnergy((MultipleChoiceQuestion) this.question));
          break;
        case ESTIMATE:
          System.out.println("Showed guess");
          Platform.runLater(() -> showGuess((EstimateQuestion) this.question));
          break;
        case HOWMUCH:
          System.out.println("Showed how much");
          Platform.runLater(() -> showHowMuch((HowMuchQuestion) this.question));
          break;
        case INSTEAD:
          System.out.println("Showed instead");
          Platform.runLater(() -> showInstead((InsteadOfQuestion) this.question));
          break;
        case INTERLEADERBOARD:
          System.out.println("Showed Intermediate Leaderboard");
          if (multiplayer) {
            server.sendScore(serverIp, new Score(clientId, name, points), gameId);
          }
          Platform.runLater(this::showIntermediateLeaderboard);
          break;
        case ENDSCREEN:
          System.out.println("Showed end screen");
          if (multiplayer) {
            server.sendScore(serverIp, new Score(clientId, name, points), gameId);
          } else {
            server.addScore(serverIp, new Score(clientId, name, points));
          }
          Platform.runLater(this::showEndScreen);
          break;
        default:
          System.out.println("Wrong question type");
          break;
      }
    }
  }

  /**
   * Shows the Guess Question
   *
   * @param question EstimateQuestion
   */
  public void showGuess(EstimateQuestion question) {
    currentQuestionCtrl = guessCtrl;
    primaryStage.getScene().setRoot(guessParent);
    guessCtrl.displayQuestion(question);
    guessCtrl.startTimer();
    this.startPointsTimer();
  }

  /**
   * Shows the WhatRequiresMoreEnergy Question
   *
   * @param question MultipleChoiceQuestion
   */
  public void showWhatRequiresMoreEnergy(MultipleChoiceQuestion question) {
    currentQuestionCtrl = whatRequiresMoreEnergyCtrl;
    primaryStage.getScene().setRoot(whatRequiresMoreEnergyParent);
    whatRequiresMoreEnergyCtrl.displayQuestion(question);
    whatRequiresMoreEnergyCtrl.startTimer();
    this.startPointsTimer();
  }

  /**
   * Shows the HowMuch Question
   *
   * @param question HowMuchQuestion
   */
  public void showHowMuch(HowMuchQuestion question) {
    currentQuestionCtrl = howMuchCtrl;
    primaryStage.getScene().setRoot(howMuchParent);
    howMuchCtrl.displayQuestion(question);
    howMuchCtrl.startTimer();
    this.startPointsTimer();
  }

  /**
   * Shows the InsteadOfQuestion
   *
   * @param question InsteadOfQuestion
   */
  public void showInstead(InsteadOfQuestion question) {
    currentQuestionCtrl = insteadOfCtrl;
    primaryStage.getScene().setRoot(insteadOfParent);
    insteadOfCtrl.displayQuestion(question);
    insteadOfCtrl.startTimer();
    this.startPointsTimer();
  }

  /**
   * Shows the IntermediateLeaderboard
   */

  public void showIntermediateLeaderboard() {
    primaryStage.getScene().setRoot(intermediateLeaderboardParent);
    intermediateLeaderboardCtrl.display();
    intermediateLeaderboardCtrl.refresh();
  }

  /**
   * Shows the ActivityList
   */
  public void showActivityList() {
    // reset name and list of players if coming out of a game
    primaryStage.getScene().setRoot(activityListParent);
    activityListCtrl.activities = server.getActivities(serverIp);
    activityListCtrl.search(activityListCtrl.searchField.getText());
  }

  /**
   * Shows the EditActivity
   */
  public void showEditActivity(Activity activity) {
    // reset name and list of players if coming out of a game
    primaryStage.getScene().setRoot(editActivityParent);
    editActivityCtrl.refresh(activity);
  }

  /**
   * Opens HelpOverlay
   */
  public void openHelp() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(helpOverlayParent);
  }

  /**
   * Closes HelpOverlay
   */
  public void closeHelp() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(helpOverlayParent);
  }

  /**
   * Opens ExitOverlay
   *
   * @param closeApp closeApp
   */
  public void openExitOverlay(boolean closeApp) {
    exitOverlayCtrl.closeApp = closeApp;
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().add(exitOverlayParent);
  }

  /**
   * Closes ExitOverlay
   */
  public void closeExitOverlay() {
    ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(exitOverlayParent);
  }

  /**
   * Shows the end screen to the user, updates the user points in the game controller
   */
  public void showEndScreen() {
    primaryStage.getScene().setRoot(endScreenParent);
    endScreenCtrl.refresh();
  }

  /**
   * Adds points
   *
   * @param toAdd points to add
   */
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
    if (!scores.iterator().hasNext()) {
      return;
    }
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
        if (maxScore.get() != 0) {
          line.setEndX(200.0 * s.getPoints() / maxScore.get());
        } else {
          line.setEndX(200);
        }
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

  /**
   * Shows joker on the screen
   */
  public void showJoker(Joker joker, String playerId) {
    System.out.println("Shown joker: " + joker + " in controller: " + currentQuestionCtrl);
    currentQuestionCtrl.showJoker(joker);
    if (joker == Joker.TIME && !clientId.equals(playerId)) {
      currentQuestionCtrl.reduceTime();
    }
  }

  /**
   * Shows emoji on the screen
   *
   * @param emoji to show
   */
  public void showEmoji(Emoji emoji) {
    // TODO: show emojis per screen
    System.out.println("Shown emoji: " + emoji + " in controller: " + currentQuestionCtrl);
    currentQuestionCtrl.showEmoji(emoji);
  }

  /**
   * Resets the game session
   */
  public void reset() {
    serverIp = null;
    clientId = null;
    gameId = null;
    waitingForGame = false;
    questionNumber = 0;
    points = 0;
    question = null;
    keepAliveExec.shutdownNow();
  }

  /**
   * Prepares the client for a new game
   */
  public void prepareForNewGame() {
    server.removePlayer(serverIp, gameId, clientId);
    gameId = null;
    waitingForGame = false;
    questionNumber = 0;
    points = 0;
    question = null;
    server.stopQuestionThread();
    usedJokers = new boolean[3];
    stopPointsTimer();
    startKeepAlive();
  }

  /**
   * Initiates and starts the keepAlive Executor Thread
   */
  public void startKeepAlive() {
    keepAliveExec = Executors.newSingleThreadScheduledExecutor();
    keepAliveExec.scheduleAtFixedRate(
      () -> {
        try {
          server.keepAlive(serverIp, clientId, waitingForGame);
        } catch (Exception e) {
          e.printStackTrace();
          Platform.runLater(this::showConnect);
          reset();
        }
      },
      0,
      1,
      TimeUnit.SECONDS
    );

  }
}
