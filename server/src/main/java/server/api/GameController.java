package server.api;

import commons.Activity;
import commons.Client;
import commons.EndScreen;
import commons.EstimateQuestion;
import commons.Game;
import commons.HowMuchQuestion;
import commons.InsteadOfQuestion;
import commons.IntermediateLeaderboardQuestion;
import commons.MultipleChoiceQuestion;
import commons.Question;
import commons.Score;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/game")
public class GameController {
  private final Random random;

  private final ActivityController activityController;
  private final PlayerController playerController;
  private final ScoreController scoreController;

  public List<Game> games = new ArrayList<>();

  /**
   * Constructor for GameController
   *
   * @param activityController access to activities
   * @param random             random generator
   * @param playerController   access to players
   * @param scoreController    access to scores
   */
  public GameController(
    ActivityController activityController,
    Random random,
    PlayerController playerController,
    ScoreController scoreController
  ) {
    this.activityController = activityController;
    this.random = random;
    this.playerController = playerController;
    this.scoreController = scoreController;
  }

  /**
   * Generates 20 questions for a new game
   *
   * @return Question array filled with random questions
   */
  private Question[] generateQuestions() {
    Question[] questions = new Question[22];

    for (int i = 0; i < 21; i++) {
      switch (random.nextInt(4)) {
        case 0:
          Activity[] activities = activityController.getRandomActivityMultiple();
          List<Activity> list = Arrays.asList(activities);
          Collections.shuffle(list);
          list.toArray(activities);
          questions[i] = new MultipleChoiceQuestion(
            activities[0],
            activities[1],
            activities[2]
          );
          break;
        case 1:
          questions[i] = new EstimateQuestion(activityController.getRandomActivity().getBody());
          break;
        case 2:
          questions[i] = new HowMuchQuestion(activityController.getRandomActivity().getBody());
          break;
        case 3:
          Activity activity1 = activityController.getRandomActivity().getBody();
          Activity activity2 = activityController.getRandomActivity().getBody();
          while (activity1.getTitle().length() > 40
            || activity1.getConsumption_in_wh() <= 0) { //limit so it doesn't run out of the screen
            activity1 = activityController.getRandomActivity().getBody();
          }
          while (activity2.getTitle().length() > 50
            || activity2.getConsumption_in_wh() <= 0) { //limit so it doesn't run out of the screen
            activity2 = activityController.getRandomActivity().getBody();
          }
          boolean finished = false;
          while (!finished) {
            if (activity1.getTitle().contains("ing ")) { //for the right formatting of the question
              if (activity2.getTitle().contains("ing ")) {
                finished = true;
              } else {
                activity2 = activityController.getRandomActivity().getBody();
              }
            } else {
              activity1 = activityController.getRandomActivity().getBody();
            }
          }
          questions[i] = new InsteadOfQuestion(activity1, activity2);
          break;
        default:
          break;
      }
      questions[i].number = i;
    }
    questions[10] = new IntermediateLeaderboardQuestion();
    questions[10].number = 10;
    questions[21] = new EndScreen();
    questions[21].number = 21;

    return questions;
  }

  /**
   * Endpoint to start a game with all players from the waiting room
   * Generates unique game id and moves all players from waiting room (the playerController) to the gameController
   *
   * @return generated game id
   */
  @PutMapping("/play")
  public synchronized String play(@RequestParam("m") boolean multiplayer) {
    String gameID = UUID.randomUUID().toString();
    System.out.println(gameID + "GAMEMULTI");
    List<Client> waiting = playerController.getPlayers().stream()
      .filter(client -> client.waitingForGame).collect(Collectors.toList());
    games.add(new Game(
      gameID,
      waiting,
      generateQuestions(),
      multiplayer
    ));
    Game thisGame = games.get(games.size() - 1);

    for (Client client : waiting) {
      client.waitingForGame = false;
      thisGame.addPlayer(client);
    }

    System.out.println(gameID);

    notifyAll();
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    initiateGame(gameID);

    return gameID;
  }

  /**
   * Starts a singleplayer game
   *
   * @return generated game id
   */
  @PostMapping("/playSingle")
  public String playSingle(@RequestBody String uid) {
    String gameID = UUID.randomUUID().toString();
    System.out.println(gameID + "GAMEIDSINGLE");
    games.add(new Game(
      gameID,
      playerController.getPlayers().stream().filter(client -> client.id.equals(uid)).collect(Collectors.toList()),
      generateQuestions(),
      false));
    initiateGame(gameID);
    return gameID;
  }

  /**
   * Sends questions to the client
   *
   * @param id game id
   */
  private void initiateGame(String id) {
    Game game = games.stream().filter(g -> g.id.equals(id)).findFirst()
      .orElseThrow(StringIndexOutOfBoundsException::new);

    game.execTiming = Executors.newSingleThreadScheduledExecutor();
    game.execTiming.submit(() -> {

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      for (int i = 0; i <= 21; i++) {
        game.playerListeners.forEach((k, l) -> {
          Question question = game.current(false);
          l.accept(question);
        });
        // Question timer
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        game.playerListeners.forEach((k, l) -> l.accept(game.current(true)));

        // Timer for break
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        game.increaseQuestionCounter();
      }
    });
  }

  /**
   * TODO: implement long polling
   * <p>
   * Endpoint to check if a user has been assigned to a game or not
   *
   * @param uid the player's id
   * @return the game id or null if not assigned yet
   */
  @PutMapping("/isActive")
  public synchronized String isActive(@RequestParam String uid) {
    try {
      do {
        wait();
      } while (games.stream()
        .noneMatch(game -> game.containsPlayer(uid)));
    } catch (InterruptedException e) {
      // Expected
    }
    return games.stream()
      .filter(game -> game.players.keySet().stream().anyMatch(client -> client.id.equals(uid))).findFirst().get().id;
  }

  /**
   * Endpoint to get a player score by id
   *
   * @return the player's score or -1 if the player is not in a game
   */

  @GetMapping("/score/{userId}")
  public int playerScore(@PathVariable String userId) {
    for (Game game : games) {
      if (game.containsPlayer(userId)) {
        return game.players.get(game.getPlayerById(userId));
      }
    }
    return -1;
  }

  /**
   * Update a score of a player
   *
   * @return the updated score or -1 if the player is not in a game
   */
  @GetMapping("/score/update/{userId}")
  public int playerScoreUpdate(@PathVariable String userId, @RequestParam int score) {
    for (Game game : games) {
      if (game.containsPlayer(userId)) {
        return game.changeScore(game.getPlayerById(userId), score);
      }
    }
    return -1;
  }

  /**
   * TODO: implement actual game sessions and non-global questionCounter
   * Returns the number of the round the player is in for a given player id
   *
   * @param id game id
   * @return round number of game
   */
  @GetMapping("/getRoundNumber/{id}")
  public int getRoundNumber(@PathVariable String id) {
    Game game = games.stream().filter(g -> g.id.equals(id)).findFirst()
      .orElseThrow(StringIndexOutOfBoundsException::new);
    return game.questionCounter;
  }

  /**
   * Gets the next question or screen in a game
   *
   * @param id ID of the game
   * @return a Result containing the Question or a NO_CONTENT http status if expired
   */
  @GetMapping("/next/{id}")
  public DeferredResult<ResponseEntity<Question>> next(
    @PathVariable String id) {
    var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    var res =
      new DeferredResult<ResponseEntity<Question>>(15000L, noContent);  //timeout after 15 seconds

    Game game = games.stream().filter(g -> g.id.equals(id)).findFirst()
      .orElseThrow(StringIndexOutOfBoundsException::new);

    var key = new Object();
    game.playerListeners.put(key, u -> res.setResult(ResponseEntity.ok(u)));

    res.onCompletion(() -> game.playerListeners.remove(key));

    return res;
  }

  /**
   * Endpoint for accepting and updating a score of the player in a game
   *
   * @param gameId game id
   * @param score  new score
   */
  @PostMapping("/score/send/{gameId}")
  public void playerScoreSend(@PathVariable("gameId") String gameId, @RequestBody Score score) {
    Game thisGame = null;
    for (Game game : games) {
      if (game.id.equals(gameId)) {
        thisGame = game;
      }
    }
    if (thisGame == null) {
      System.out.println("Didnt find the game");
    }
    Client player = new Client(score.getPlayer(), score.getName(), false);
    for (Client client : thisGame.getPlayers().keySet()) {
      if (player.equals(client)) {
        player = client;
      }
    }
    thisGame.getPlayers().put(player, score.points);
    if (thisGame.isMultiplayer() == false && thisGame.questionCounter >= 21) {
      scoreController.addScore(score);
    }

  }

  /**
   * Returns a list of score for a multiplayer leaderboard for one game
   *
   * @param gameId specific game
   * @return list of scores
   */
  @GetMapping("/multiLeaderboard/{gameId}")
  public Iterable<Score> getMultiLeaderboard(@PathVariable("gameId") String gameId) {
    Game currentGame = null;
    for (Game game : games) {
      if (game.id.equals(gameId)) {
        currentGame = game;
      }
    }
    if (currentGame == null) {
      System.out.println("GameId not found for the multiplayer leaderboard");
      return null;
    }
    List<Score> scores = new LinkedList<>();
    Score toBeAdded;
    for (Client player : currentGame.getPlayers().keySet()) {
      toBeAdded = new Score(player.id, player.username, currentGame.getPlayers().get(player));
      scores.add(toBeAdded);
    }
    return scores;
  }
}

