package server.api;

import commons.EstimateQuestion;
import commons.HowMuchQuestion;
import commons.MultipleChoiceQuestion;
import commons.Question;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.QuestionRepository;

@RestController
@RequestMapping("/api/game")
public class GameController {

  private final ActivityController activityController;
  private final Random random;
  private static int questionCounter = 0;
  private final QuestionRepository questionRepository;
  private final PlayerController playerController;


  private ExecutorService timerThreads = Executors.newFixedThreadPool(10);

  private final ScoreController scoreController;
  private String uniqueServerId;


  /**
   * Maps the unique game ID with a Pair of < username, points >
   */
  private Map<String, Pair<String, Integer>> games = new HashMap<>();

  public GameController(ActivityController activityController, Random random,
                        QuestionRepository questionRepository, PlayerController playerController,
                        ScoreController scoreController) {
    this.activityController = activityController;
    this.random = random;
    this.questionRepository = questionRepository;
    this.playerController = playerController;
    this.scoreController = scoreController;
  }

  /**
   * Endpoint to start a game with all players from the waiting room
   * Generates unique game id and moves all players from waiting room (the playerController) to the gameController
   *
   * @return generated game id
   */
  @PostMapping("/play")
  public String play() {
    String uniqueServerID = UUID.randomUUID().toString();

    playerController.getPlayers().forEach(p -> {
      games.put(uniqueServerID, Pair.of(p, 0));
    });
    this.uniqueServerId = uniqueServerID;
    return uniqueServerID;
  }

  /**
   * TODO: implement long polling
   * <p>
   * Endpoint to check if a user has been assigned to a game or not
   *
   * @param userID - unique id of player
   * @return the game id or null if not assigned yet
   */
  @PutMapping("/isGameActive")
  public String isGameActive(@RequestBody String userID) {
    for (String key : games.keySet()) {
      if (games.get(key).getFirst().equals(userID)) {
        return key;
      }
    }
    return null;
  }

  /**
   * Endpoint to get the next question
   * Randomly selects a type of question and return it
   * <p>
   * Returns NULL to indicate the end of the game
   *
   * @return ResponseEntity < Question > or null
   */
  @GetMapping("/next")
  public ResponseEntity<Question> nextStep() {
    if (questionCounter >= 20) {
      return ResponseEntity.ok(null);  // game ended
    } else {
      questionCounter++;
    }
    Question question = null;

    switch (random.nextInt(3)) {

      case 0: // Multiple choice
        question = new MultipleChoiceQuestion(
          activityController.getRandomActivity().getBody(),
          activityController.getRandomActivity().getBody(),
          activityController.getRandomActivity().getBody()
        );
        break;

      case 1: // Estimate
        question = new EstimateQuestion(activityController.getRandomActivity().getBody());
        break;

      case 2: // How much
        question = new HowMuchQuestion(activityController.getRandomActivity().getBody());
        break;

      default:
        break;
    }
    //questionRepository.save(question);
    return ResponseEntity.ok(question);
  }


  @GetMapping("/finished/{duration}")
  public DeferredResult<Boolean> serverTimerStart(@PathVariable(name = "duration") long duration) {
    DeferredResult<Boolean> result = new DeferredResult<>();
    System.out.println("timing");
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
   * @return int score or -1 if the player is not in the game
   */
  @GetMapping("/{id}/score")
  public int playerScore(@PathVariable("id") String id) {
    String player = playerController.clients.get(id).getSecond();
    for (String key : games.keySet()) {
      if (games.get(key).getFirst().equals(player)) {
        return games.get(key).getSecond();
      }
    }
    return -1;

  }

  /**
   * Update a score of a player
   *
   * @return true if the score was updated or false if the player is not in the game
   */
  @GetMapping("/{id}/score/update")
  public boolean playerScoreUpdate(@PathVariable("id") String id) {
    String player = playerController.clients.get(id).getSecond();
    int oldScore = playerScore(id);
    if (oldScore == -1) {
      return false;
    } else {
      games.replace(this.uniqueServerId, Pair.of(player, oldScore + 1));
      //rn everytime we update the score it just goes by one, this will likely be changed in later versions
      return true;
    }


  }
}
