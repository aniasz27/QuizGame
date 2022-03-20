package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Random;

@SuppressWarnings("unused")
@JsonTypeName("HOWMUCH")
public class HowMuchQuestion extends Question {

  private String question;
  private Activity activity;
  private long wrong1;
  private long wrong2;
  private boolean[] correct;
  private long[] answers;

  private final Random random;

  public HowMuchQuestion() {
    super(Type.HOWMUCH);
    this.random = new Random();
  }

  public HowMuchQuestion(Activity activity) {
    super(Type.HOWMUCH);
    this.activity = activity;
    this.random = new Random();
    this.question = "How much energy does this activity use?";
    this.correct = new boolean[3];
    this.answers = new long[3];
    computeWrong();
    setRandomAnswers();
  }

  private void computeWrong() {
    long correct = this.activity.consumption_in_wh;
    int factor = 0;
    while (factor < 10) { //make factor at least 10
      factor = random.nextInt(50); // percentage to modify
    }
    int direction = random.nextInt(2); // 0 for going down, 1 for up

    if (direction == 0) {
      wrong1 = correct - correct * (100 - factor) / 100L;
    } else {
      wrong1 = correct + correct * (100 - factor) / 100L;
    }

    factor = 0;
    while (factor < 10) { //make factor at least 10
      factor = random.nextInt(50); // percentage to modify
    }
    direction = random.nextInt(2); // 0 for going down, 1 for up

    if (direction == 0) {
      wrong2 = correct - correct * (100 - factor) / 100L;
    } else {
      wrong2 = correct + correct * (100 - factor) / 100L;
    }
  }

  public void setRandomAnswers() {
    int place = random.nextInt(3);
    answers[place] = this.activity.consumption_in_wh;
    answers[(place + 1) % 3] = wrong1;
    answers[(place + 2) % 3] = wrong2;
    correct[place] = true;
  }

  public boolean[] getCorrect() {
    return correct;
  }

  public long[] getAnswers() {
    return answers;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public long getWrong1() {
    return wrong1;
  }

  public void setWrong1(long wrong1) {
    this.wrong1 = wrong1;
  }

  public long getWrong2() {
    return wrong2;
  }

  public void setWrong2(long wrong2) {
    this.wrong2 = wrong2;
  }
}
