package commons;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MultipleChoiceQuestionTest {
  static Activity activity1;
  static Activity activity2;
  static Activity activity3;
  static MultipleChoiceQuestion question;

  @BeforeAll
  static void setUp() {
    activity1 = new Activity("act1", "author", "img1", "Doing something", 1234, "www.source.com/1");
    activity2 = new Activity("act2", "author", "img2", "Doing something else ", 456, "www.source.com/2");
    activity3 = new Activity("act3", "author1", "img3", "Doing stuff", 123412, "www.source.com/3");
    question = new MultipleChoiceQuestion(activity1, activity2, activity3);
  }

  @Test
  void constructorTest() {
    MultipleChoiceQuestion q = new MultipleChoiceQuestion();
    assertEquals(Question.Type.MULTICHOICE, q.getType());
  }

  @Test
  void getQuestion() {
    assertEquals("Which of the following activities consumes the most energy?", question.getQuestion());
  }

  @Test
  void getActivity1() {
    assertEquals(activity1, question.getActivity1());
  }

  @Test
  void getActivity2() {
    assertEquals(activity2, question.getActivity2());
  }

  @Test
  void getActivity3() {
    assertEquals(activity3, question.getActivity3());
  }

  @Test
  void getCorrect() {
    boolean[] correct = {false, false, true};
    assertArrayEquals(correct, question.getCorrect());
  }

  @Test
  void getCorrectDifferentOrder() {
    MultipleChoiceQuestion q = new MultipleChoiceQuestion(activity3, activity2, activity1);
    boolean[] correct = {true, false, false};
    assertArrayEquals(correct, q.getCorrect());
  }
}