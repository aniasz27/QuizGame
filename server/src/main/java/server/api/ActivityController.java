package server.api;

import commons.Activity;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;


/**
 * Class controls the repository for the activity bank
 * Maping at /api/activity
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityRepository repo;
  private final Random random;

  private final int firstNumLimit = 0;
  private final int secondNumLimit = 0;

  /**
   * Constructor
   *
   * @param repo   dependency injection
   * @param random dependency injection
   */
  @Autowired
  public ActivityController(ActivityRepository repo, Random random) {
    this.repo = repo;
    this.random = random;
  }

  /**
   * GET for all the activities
   *
   * @return list of activities from db
   */
  @GetMapping("/list")
  public Iterable<Activity> getAll() {
    return repo.findAll();
  }


  /**
   * Get a random activity from the question bank.
   * See firstNumLimit and secondNumLimit for the maximum group number
   *
   * @return a response entity with the selected Activity
   */
  @GetMapping("/random")
  public ResponseEntity<Activity> getRandomActivity() {
    int firstNum = random.nextInt(firstNumLimit + 1);
    int secondNum = random.nextInt(secondNumLimit + 1);
    String group = String.valueOf(firstNum) + String.valueOf(secondNum) + "%";
    int counter = random.nextInt(repo.getRandomActivityCount(group));
    return ResponseEntity.ok(repo.getRandomActivity(group, counter));
  }


  /**
   * Method used for saving all activities in the db
   *
   * @param activityList list of activities
   */
  public void saveAll(List<Activity> activityList) {
    repo.saveAll(activityList);
  }

  /**
   * Method to clear the activity bank db
   */
  public void deleteAll() {
    repo.deleteAll();
  }
}
