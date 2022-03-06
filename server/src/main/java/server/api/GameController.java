package server.api;

import commons.EstimateQuestion;
import commons.MultipleChoiceQuestion;
import commons.Question;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.QuestionRepository;

@RestController
@RequestMapping("/api/game")
public class GameController {

  private final ActivityController activityController;
  private final Random random;
  private static int questionCounter = 0;
  private final QuestionRepository questionRepository;
  private final PlayerController playerController;


  /**
   * Maps the unique game ID with a Pair of < username, points >
   */
  private Map<String, Pair<String, Integer>> games = new HashMap<>();

  public GameController(ActivityController activityController, Random random,
                        QuestionRepository questionRepository, PlayerController playerController) {
    this.activityController = activityController;
    this.random = random;
    this.questionRepository = questionRepository;
    this.playerController = playerController;
  }

  @PostMapping("/play")
  public String play() {
    String uniqueServerID = UUID.randomUUID().toString();

    playerController.getPlayes().forEach(p -> {
      games.put(uniqueServerID, Pair.of(p, 0));
    });

    return uniqueServerID;
  }

  @PutMapping("/isGameActive")
  public String isGameActive(@RequestBody String userID) {
    for (String key : games.keySet()) {
      if (games.get(key).getFirst().equals(userID)) {
        return key;
      }
    }
    return null;
  }

  @GetMapping("/next")
  public ResponseEntity<Question> nextStep() {
    if (questionCounter >= 20) {
      return ResponseEntity.ok(null);  // game ended
    } else {
      questionCounter++;
    }
    Question question = null;

    switch (random.nextInt(2)) {
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
      case 2: // Type 3

        break;
      default:
        break;
    }
    //questionRepository.save(question);
    return ResponseEntity.ok(question);
  }


}
