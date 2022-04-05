package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InsteadOfTest {
  private static InsteadOfQuestion question;

  @BeforeAll
  public static void setup() {
    Activity activity1 = new Activity("act1", "author", "img1", "Doing something", 1234, "www.source.com/1");
    Activity activity2 = new Activity("act2", "author", "img2", "Doing something else", 456, "www.source.com/2");
    question = new InsteadOfQuestion(activity1, activity2);
  }

  @Test
  public void emptyConstructorTest() {
    InsteadOfQuestion question = new InsteadOfQuestion();
    assertNotNull(question);
  }

  @Test
  public void constructorTest() {
    assertNotNull(question);
  }

  @Test
  public void factorGetter() {
    long correct = Math.round(1234.0 / 456);
    assertEquals(correct, question.getFactor());
  }

  @Test
  public void getTitle1Test() {
    String correct = "Instead of doing something, ";
    assertEquals(correct, question.getTitle1());
  }

  @Test
  public void getTitle2Test() {
    String correct = "doing something else ?";
    assertEquals(correct, question.getTitle2());
  }

  @Test
  public void getActivity1Test() {
    Activity activity1 = new Activity("act1", "author", "img1", "Doing something", 1234, "www.source.com/1");
    assertEquals(activity1, question.getActivity1());
  }

  @Test
  public void getActivity2Test() {
    Activity activity2 = new Activity("act2", "author", "img2", "Doing something else", 456, "www.source.com/2");
    assertEquals(activity2, question.getActivity2());
  }

  @Test
  public void calculateHowCloseTest1() {
    int value = 3;
    assertEquals(100, question.calculateHowClose(value));
  }

  @Test
  public void calculateHowCloseTest2() {
    int value = 9;
    assertEquals(0, question.calculateHowClose(value));
  }


}
