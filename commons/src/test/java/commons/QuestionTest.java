package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QuestionTest {

  @Test
  void getType() {
    Question q = new HowMuchQuestion();
    assertEquals(Question.Type.HOWMUCH, q.getType());
  }

  @Test
  void testTypeEquals() {
    Question q1 = new EstimateQuestion();
    assertTrue(q1.getType().equals(Question.Type.ESTIMATE));
  }

  @Test
  void testTypeEqualsFalse() {
    Question q1 = new EstimateQuestion();
    assertFalse(q1.getType().equals(Question.Type.HOWMUCH));
  }

  @Test
  public void calculateHowCloseTest1() {
    Question question = new EstimateQuestion();
    int value = 3;
    assertEquals(100, question.calculateHowClose(value, 3));
  }

  @Test
  public void calculateHowCloseTest2() {
    Question question = new EstimateQuestion();
    int value = 9;
    assertEquals(0, question.calculateHowClose(value, 3));
  }
}