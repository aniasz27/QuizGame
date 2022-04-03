package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.utils.ServerUtils;
import commons.Activity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActivityListCtrlTest {
  private MainCtrl sut;
  private ServerUtils server;

  private ActivityListCtrl ctrl;

  List<Activity> activities = Arrays.asList(
    new Activity("1", "a", "0", "first", 1, "s0"),
    new Activity("2", "b", "1", "second", 2, "s1"),
    new Activity("3", "c", "2", "third", 3, "s2")
  );

  List<Activity> searchResults = new ArrayList<>();

  @BeforeEach
  public void setup() {
    sut = new MainCtrl(server);
    ctrl = new ActivityListCtrl(server, sut);
    ctrl.activities = activities;
  }

  @Test
  public void searchIdTest() {
    String query = "id:1";
    try {
      searchResults = ctrl.search(query);
    } catch (NullPointerException e) {
      // Expected, as some fxml elements aren't initialised
    }
    assertEquals(searchResults, Collections.singletonList(activities.get(0)));
  }

  @Test
  public void searchAuthorTest() {
    String query = "a:b";
    try {
      searchResults = ctrl.search(query);
    } catch (NullPointerException e) {
      // Expected, as some fxml elements aren't initialised
    }
    assertEquals(searchResults, Collections.singletonList(activities.get(1)));
  }

  @Test
  public void searchTitleTest() {
    String query = "t:third";
    try {
      searchResults = ctrl.search(query);
    } catch (NullPointerException e) {
      // Expected, as some fxml elements aren't initialised
    }
    assertEquals(searchResults, Collections.singletonList(activities.get(2)));
  }

  @Test
  public void searchSourceTest() {
    String query = "s:s0";
    try {
      searchResults = ctrl.search(query);
    } catch (NullPointerException e) {
      // Expected, as some fxml elements aren't initialised
    }
    assertEquals(searchResults, Collections.singletonList(activities.get(0)));
  }

  @Test
  public void searchConsumptionTest() {
    String query = "c=2";
    try {
      searchResults = ctrl.search(query);
    } catch (NullPointerException e) {
      // Expected, as some fxml elements aren't initialised
    }
    assertEquals(searchResults, Collections.singletonList(activities.get(1)));
  }

}
