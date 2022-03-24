package server.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commons.Activity;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private List<Activity> sortedActivities;

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
    if (sortedActivities == null) {
      sortedActivities = repo.getSortedActivities();
    }
    int counter = random.nextInt(sortedActivities.size());
    return ResponseEntity.ok(sortedActivities.get(counter));
  }

  /**
   * Picks a random activity and sends back 3 activities - random, one lower and one higher
   *
   * @return an Activity array consisting of 3 activities with reasonably close consumption
   */
  public Activity[] getRandomActivityMultiple() {
    if (sortedActivities == null) {
      sortedActivities = repo.getSortedActivities();
    }
    Activity[] activities = new Activity[3];
    int counter = random.nextInt(sortedActivities.size() + 1);
    activities[0] = sortedActivities.get(counter);
    if (counter < 3) {
      goHigher(1, activities, counter + 1);
      goHigher(2, activities, counter + 2);

    } else if (counter > sortedActivities.size() - 2) {
      goLower(1, activities, counter - 1);
      goLower(2, activities, counter - 2);
    } else {
      goLower(1, activities, counter - 1);
      goHigher(2, activities, counter + 1);
    }
    return activities;
  }

  public void goLower(int place, Activity[] activities, int start) {
    int current = start;
    Activity toBeAdded;
    boolean added = false;
    while (!added) {
      toBeAdded = sortedActivities.get(current);
      if (place == 2) {
        if (toBeAdded.getConsumption_in_wh() != activities[0].getConsumption_in_wh()
          && toBeAdded.getConsumption_in_wh() != activities[1].getConsumption_in_wh()) {
          activities[place] = toBeAdded;
          added = true;
          return;
        }
      } else {
        if (toBeAdded.getConsumption_in_wh() != activities[0].getConsumption_in_wh()) {
          activities[place] = toBeAdded;
          added = true;
          return;
        }
      }
      current--;
    }

  }

  public void goHigher(int place, Activity[] activities, int start) {
    int current = start;
    Activity toBeAdded;
    boolean added = false;
    while (!added) {
      toBeAdded = sortedActivities.get(current);
      if (place == 2) {
        if (toBeAdded.getConsumption_in_wh() != activities[0].getConsumption_in_wh()
          && toBeAdded.getConsumption_in_wh() != activities[1].getConsumption_in_wh()) {
          activities[place] = toBeAdded;
          added = true;
          return;
        }
      } else {
        if (toBeAdded.getConsumption_in_wh() != activities[0].getConsumption_in_wh()) {
          activities[place] = toBeAdded;
          added = true;
          return;
        }
      }
      current++;
    }

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
    repo.save(activity);
    sortedActivities = repo.getSortedActivities();
    return ResponseEntity.ok(activity);
  }

  /**
   * Endpoint to re-import all activities from the activities.json file located at
   * src/main/resources/JSON/activities.json
   *
   * @return confirmation message or error
   */

  @GetMapping("/importActivities")
  public ResponseEntity<String> importAllActivities() {

    try {
      String activitiesPath = "/JSON/activities.json";
      Gson gson = new Gson();
      Reader reader = new InputStreamReader(getClass().getResourceAsStream(activitiesPath));
      List<Activity> activities = gson.fromJson(reader, new TypeToken<List<Activity>>() {
      }.getType());
      reader.close();
      repo.saveAll(activities);
      sortedActivities = repo.getSortedActivities();
      return ResponseEntity.ok("Activities imported successfully!");
    } catch (IOException e) {
      System.err.println("Something went wrong when importing activities:");
      e.printStackTrace();
      return ResponseEntity.internalServerError()
        .body("Something went wrong when importing activities: " + e.getMessage());
    }
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
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
        .body("Something went wrong when importing activities: " + e.getMessage());
    }

    return ResponseEntity.ok("Activities imported successfully!");
  }

  @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> getImage(@PathVariable String id) {
    try {
      InputStream imageStream = getClass().getResourceAsStream(
        "/JSON/" + repo.findById(id).orElseThrow(IOException::new).getImage_path()
      );
      return ResponseEntity.ok(imageStream.readAllBytes());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
