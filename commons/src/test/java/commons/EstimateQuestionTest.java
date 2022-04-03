package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class EstimateQuestionTest {

  static EstimateQuestion question;

  @BeforeAll
  public static void setUp() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity = new Activity(id, author, image, title, consumption, source);
    question = new EstimateQuestion(activity);
  }


  @Test
  public void constructorTest() {
    assertNotNull(question);
  }

  @Test
  public void getQuestionTest() {
    String question1 = "How much does this activity take?";
    assertEquals(question1, question.getQuestion());
  }

  @Test
  public void getActivityTest() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity = new Activity(id, author, image, title, consumption, source);
    assertEquals(activity, question.getActivity());
  }

  @Test
  public void getAnswerTest() {
    long answer = question.getActivity().getConsumption_in_wh();
    assertEquals(answer, question.getAnswer());
  }

  @Test
  public void equalsSame() {
    assertEquals(question, question);
  }

  @Test
  public void equalsNotEstimateQuestion() {
    assertNotEquals(question, new Score("uuid", "baba", 10));
  }

  @Test
  public void equalsEqual() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 3000;
    String source = "someURL";
    Activity activity = new Activity(id, author, image, title, consumption, source);
    Question question1 = new EstimateQuestion(activity);
    assertEquals(question1, question);
  }

  @Test
  public void equalsDifferent() {
    String id = "RandomID";
    String author = "Group00";
    String image = "imagePath";
    String title = "Title of the Activity";
    long consumption = 300;
    String source = "someURL";
    Activity activity = new Activity(id, author, image, title, consumption, source);
    Question question1 = new EstimateQuestion(activity);
    assertNotEquals(question1, question);
  }

  @Test
  public void toStringTest() {
    String answer =
      "EstimateQuestion{question='How much does this activity take?',"
        + " activity=Activity{id='RandomID', author='Group00', image_path='imagePath',"
        + " title='Title of the Activity', consumption_in_wh=3000, source='someURL'}}";
    assertEquals(answer, question.toString());
  }

  @Test
  public void calculateHowCloseCorrect() {
    long answer = 2900;
    assertTrue(question.calculateHowClose(2900) > 0);
  }

  @Test
  public void calculateHowCloseBelow() {
    long answer = 2300;
    assertFalse(question.calculateHowClose(answer) > 0);
  }

  @Test
  public void calculateHowCloseAbove() {
    long answer = 3700;
    assertFalse(question.calculateHowClose(answer) > 0);
  }
}
