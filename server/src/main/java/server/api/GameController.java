package server.api;

import commons.Activity;
import commons.Client;
import commons.EstimateQuestion;
import commons.Game;
import commons.HowMuchQuestion;
import commons.MultipleChoiceQuestion;
import commons.Question;
import commons.Score;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  private final ExecutorService timerThreads = Executors.newFixedThreadPool(10);

  private final List<Game> games = new ArrayList<>();

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

  private Question[] generateQuestions() {
    Question[] questions = new Question[20];

    for (int i = 0; i < 20; i++) {
      switch (random.nextInt(3)) {
        case 0:
          Activity[] activities = activityController.getRandomActivityMultiple();
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
        default:
          break;
      }
      questions[i].number = i;
    }

    return questions;
  }

  /**
   * Endpoint to start a game with all players from the waiting room
   * Generates unique game id and moves all players from waiting room (the playerController) to the gameController
   *
   * @return generated game id
   */
  @PostMapping("/play")
  public synchronized String play() {
    String gameID = UUID.randomUUID().toString();
    List<Client> waiting = playerController.getPlayers().stream()
      .filter(client -> client.waitingForGame).collect(Collectors.toList());
    games.add(new Game(
      gameID,
      waiting,
      generateQuestions()
    ));

    for (Client client : waiting) {
      client.waitingForGame = false;
    }

    System.out.println(gameID);
    System.out.println(games);

    notifyAll();

    return gameID;
  }

  /**
   * Starts a singleplayer game
   *
   * @return generated game id
   */
  @PostMapping("/playSingle")
  public String playSingle(@RequestParam String uid) {
    String gameID = UUID.randomUUID().toString();
    games.add(new Game(
      gameID,
      playerController.getPlayers().stream().filter(client -> client.id.equals(uid)).collect(Collectors.toList()),
      generateQuestions()
    ));
    return gameID;
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
   * Gets the next question or screen in a game
   *
   * @param id             ID of the game
   * @param questionNumber the number of the question the client is currently on
   * @return the next screen, with status 410 if the game ended
   */
  @GetMapping("/next/{id}")
  public ResponseEntity<Question> next(
    @PathVariable String id,
    @RequestParam("q") int questionNumber
  ) {
    System.out.println(id);
    System.out.println(games);

    Game game = games.stream().filter(g -> g.id.equals(id)).findFirst()
      .orElseThrow(StringIndexOutOfBoundsException::new);

    Question question;
    if (questionNumber < game.questionCounter) {
      question = game.current();
    } else {
      question = game.next();
    }
    System.out.println(question.type);
    if (question.type.equals(Question.Type.ENDSCREEN)) {
      if (game.players.size() == 1) {
        Client player = (Client) game.players.keySet().toArray()[0];
        scoreController.addScore(new Score(player.id, player.username, game.players.get(player)));
      }
      return ResponseEntity.status(HttpStatus.GONE).body(question);
    }

    return ResponseEntity.ok(question);
  }

  @GetMapping("/startTimer/{id}")
  public DeferredResult<Boolean> serverTimerStart(@PathVariable String id, @RequestParam long duration) {
    DeferredResult<Boolean> result = new DeferredResult<>();
    timerThreads.execute(() -> {
      try {
        Thread.sleep(duration);
        result.setResult(true);
      } catch (InterruptedException e) {
        e.printStackTrace();
        result.setErrorResult(false);
      }
    });

    return result;
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
}

