package server.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commons.Activity;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;


/**
 * Class controls the repository for the activity bank
 * Mapping at /api/activity
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityRepository repo;
  private final Random random;

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
  @SuppressWarnings("all")
  public ResponseEntity<Activity> getRandomActivity() {
    int firstNumLimit = 7;
    int secondNumLimit = 9;

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

  /**
   * Creates or updates an activity in the db
   * If the id in the request body already exists, it will update
   * If not it will create a new activity
   *
   * @param activity The activity to add or update in json format
   * @return The created or updated activity
   */
  @PutMapping("/update")
  public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity) {
    return ResponseEntity.ok(repo.save(activity));
  }

  /**
   * Endpoint to re-import all activities from the activities.json file located at
   * server/src/main/resources/JSON/activities.json
   *
   * @return confirmation message or error
   */

  @GetMapping("/importActivities")
  public ResponseEntity<String> importAllActivities() {

    try {
      String activitiesPath = "src/main/resources/JSON/activities.json";
      Gson gson = new Gson();
      Reader reader = Files.newBufferedReader(Paths.get(activitiesPath));
      List<Activity> activities = gson.fromJson(reader, new TypeToken<List<Activity>>() {
      }.getType());
      reader.close();
      repo.saveAll(activities);
    } catch (IOException e) {
      System.err.println("Something went wrong when importing activities:");
      e.printStackTrace();
    }

    return ResponseEntity.ok("Activities imported successfully!");
  }

  @PostMapping("/importActivitiesFromFile")
  public ResponseEntity<String> importAllActivitiesFromFile(@RequestBody String filePath) {

    try {
      Gson gson = new Gson();
      Reader reader = Files.newBufferedReader(Paths.get(filePath));
      List<Activity> activities = gson.fromJson(reader, new TypeToken<List<Activity>>() {
      }.getType());
      reader.close();
      repo.saveAll(activities);
    } catch (IOException e) {
      System.out.println("Something went wrong when importing activities: " + e.getMessage());
    }

    return ResponseEntity.ok("Activities imported successfully!");
  }


}
