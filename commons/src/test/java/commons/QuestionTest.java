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
}