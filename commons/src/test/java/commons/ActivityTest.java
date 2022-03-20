package commons;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


public class ActivityTest {

  static Activity testActivity;

  @BeforeAll
  public static void setUp() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    testActivity = new Activity(id, author, image, title, consumption, source);
  }


  @Test
  public void constructorTest() {
    assertNotNull(testActivity);
  }

  @Test
  public void getIdTest() {
    assertEquals("RandomID", testActivity.getId());
  }

  @Test
  public void getAuthorTest() {
    assertEquals("Group00", testActivity.getAuthor());
  }

  @Test
  public void getImageTest() {
    assertEquals("imagePath", testActivity.getImage_path());
  }

  @Test
  public void getTitleTest() {
    assertEquals("Title of the Activity", testActivity.getTitle());
  }

  @Test
  public void getConsumptionTest() {
    assertEquals(3000, testActivity.getConsumption_in_wh());
  }

  @Test
  public void getSourceTest() {
    assertEquals("someURL", testActivity.getSource());
  }

  @Test
  public void equalsSameTest() {
    assertEquals(testActivity, testActivity);
  }

  @Test
  public void equalsSameButDifferentTest() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferent() {
    String id = "RandomID";
    String author = "Group0"; // one 0 missing
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertFalse(testActivity.equals(activity2));
  }

  @Test
  public void toStringTest() {
    assertEquals(
      "Activity{id='RandomID', author='Group00', image_path='imagePath', title='Title of the Activity',"
        + " consumption_in_wh=3000, source='someURL'}",
      testActivity.toString());
  }


}
