package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void getWrong1() {
  }

  @Test
  void setWrong1() {
  }

  @Test
  void getWrong2() {
  }

  @Test
  void setWrong2() {
  }
}