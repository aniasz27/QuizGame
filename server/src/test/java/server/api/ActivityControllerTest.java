package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import commons.Activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;


public class ActivityControllerTest {

  private TestActivityRepository repo;
  private ActivityController controller;

  @BeforeEach
  public void setup() {
    repo = new TestActivityRepository();
    Random random = new Random();
    controller = new ActivityController(repo, random);
  }

  @Test
  public void constructorTest() {
    assertNotNull(controller);
  }

  @Test
  public void saveAllTest() {
    List<Activity> activities = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      activities.add(new Activity());
    }
    controller.saveAll(activities);
    assertNotNull(controller.getAll());
  }

  @Test
  public void getAllTest() {
    List<Activity> activities = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      activities.add(new Activity());
    }
    controller.saveAll(activities);
    assertEquals(activities, controller.getAll());
  }

  @Test
  public void getRandomActivityTest() {
    List<Activity> activities = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      activities.add(new Activity());
    }
    repo.setSortedActivities((ArrayList<Activity>) activities);
    ResponseEntity<Activity> activity = controller.getRandomActivity();
    assertNotNull(controller.sortedActivities);
    assertNotNull(activity);
  }

  @Test
  public void findActivityTest() {
    List<Activity> activities = new ArrayList<>();
    Activity a2 = new Activity("id", "author", "Path", "title", 300, "source");
    Activity a1 = new Activity("id", "author", "Path", "title", 400, "source");
    Activity a3 = new Activity("id", "author", "Path", "title", 500, "source");
    activities.add(a1);
    activities.add(a2);
    activities.add(a3);
    controller.sortedActivities = repo.setSortedActivities((ArrayList<Activity>) activities);
    Activity[] array = new Activity[3];
    array[0] = a1;
    controller.findActivity(1, array, 0, 1);
    assertNotNull(array[1]);
  }

  @Test
  public void getRandomActivityMultiple() {
    List<Activity> activities = new ArrayList<>();
    Activity a2 = new Activity("id", "author", "Path", "title", 300, "source");
    Activity a1 = new Activity("id", "author", "Path", "title", 400, "source");
    Activity a3 = new Activity("id", "author", "Path", "title", 500, "source");
    for (int i = 0; i < 20; i++) {
      activities.add(a1);
      activities.add(a2);
      activities.add(a3);
    }
    controller.sortedActivities = repo.setSortedActivities((ArrayList<Activity>) activities);
    Activity[] check = controller.getRandomActivityMultiple();
    assertNotNull(check);
  }

  @Test
  public void deleteAllTest() {
    List<Activity> activities = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      activities.add(new Activity());
    }
    controller.deleteAll();
    assertNull(controller.getAll());
  }

  @Test
  public void updateTest() {
    Activity activity = new Activity();
    repo.activities = new ArrayList<>();
    assertEquals(ResponseEntity.ok(activity), controller.updateActivity(activity));
  }


}
