package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HowMuchQuestionTest {
  static Activity a;

  @BeforeAll
  static void beforeAll() {
    a = new Activity("act", "author", "img", "Doing something", 1234, "www.source.com");
  }

  @Test
  void getQuestion() {
    HowMuchQuestion q = new HowMuchQuestion(a);
    assertEquals("How much energy does this activity use?", q.getQuestion());
  }

  @Test
  void setQuestion() {
    HowMuchQuestion q = new HowMuchQuestion();
    q.setQuestion("Question?");
    assertEquals("Question?", q.getQuestion());
  }

  @Test
  void getActivity() {
    HowMuchQuestion q = new HowMuchQuestion(a);
    assertEquals(a, q.getActivity());
  }

  @Test
  void setActivity() {
    HowMuchQuestion q = new HowMuchQuestion();
    q.setActivity(a);
    assertEquals(a, q.getActivity());
  }

  @Test
  void getAndSetWrong1() {
    HowMuchQuestion q = new HowMuchQuestion();
    q.setWrong1(1234);
    assertEquals(1234, q.getWrong1());
  }

  @Test
  void getAndSetWrong2() {
    HowMuchQuestion q = new HowMuchQuestion();
    q.setWrong2(1234);
    assertEquals(1234, q.getWrong2());
  }

  @Test
  void testOnlyOneCorrectAnswer() {
    HowMuchQuestion q = new HowMuchQuestion(a);
    long correctAnswer = q.getActivity().getConsumption_in_wh();
    boolean[] correct = q.getCorrect();
    int correctAnswers = 0;

    for (int i = 0; i < correct.length; i++) {
      if (correct[i]) {
        assertEquals(correctAnswer, q.getAnswers()[i]);
        correctAnswers++;
      } else {
        assertNotEquals(correctAnswer, q.getAnswers()[i]);
      }
    }

    assertEquals(1, correctAnswers);
  }

  @Test
  void testCorrectRange() {
    HowMuchQuestion q = new HowMuchQuestion(a);
    long correctAnswer = q.getActivity().getConsumption_in_wh();
    long wrong1 = q.getWrong1();
    long wrong2 = q.getWrong2();

    long diff1 = Math.abs(correctAnswer - wrong1);
    assertTrue(correctAnswer * (50) / 100L <= diff1);
    assertTrue(diff1 <= correctAnswer * (90) / 100L);

    long diff2 = Math.abs(correctAnswer - wrong2);
    assertTrue(correctAnswer * (50) / 100L <= diff2);
    assertTrue(diff2 <= correctAnswer * (90) / 100L);
  }
}