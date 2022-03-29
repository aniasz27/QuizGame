package commons;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


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
  public void equalsNotActivity() {
    assertNotEquals(testActivity, new Score("uuid", "baba", 10));
  }

  @Test
  public void equalsEqual() {
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
  public void equalsDifferentConsumption() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 300;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferentId() {
    String id = "NotAtAllRandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferentAuthor() {
    String id = "RandomID";
    String author = "GroupNot00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferentImage() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath???";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferentTitle() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "les gooooooo";
    long consumption = 3000;
    String source = "someURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void equalsDifferentSource() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someOtherURL";
    Activity activity2 = new Activity(id, author, image, title, consumption, source);
    assertNotEquals(testActivity, activity2);
  }

  @Test
  public void hashCodeSameTest() {
    assertEquals(testActivity.hashCode(), testActivity.hashCode());
  }

  @Test
  public void toStringTest() {
    assertEquals(
      "Activity{id='RandomID', author='Group00', image_path='imagePath', title='Title of the Activity',"
        + " consumption_in_wh=3000, source='someURL'}",
      testActivity.toString());
  }
}
