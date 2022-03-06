package commons;

import static commons.Question.Type;

import java.util.Arrays;

@SuppressWarnings("unused")
public class MultipleChoiceQuestion extends Question {

  private String question;
  private Activity[] activities;
  private boolean[] correct;

  public MultipleChoiceQuestion() {
    super(Type.MULTICHOICE);
  }

  public MultipleChoiceQuestion(Activity activity1, Activity activity2, Activity activity3) {
    super(Type.MULTICHOICE);
    this.question = "Which of the following activities consumes the most energy?";
    this.activities = new Activity[3];
    this.activities[0] = activity1;
    this.activities[1] = activity2;
    this.activities[2] = activity3;
    this.correct = new boolean[3];
    computeCorrectAnswer();
  }

  /**
   * Fill the boolean[] correct
   */
  private void computeCorrectAnswer() {
    long maxWh =
      Arrays.stream(activities).mapToLong(a -> a.consumption_in_wh).max().orElse(0);
    for (int i = 0; i < 3; i++) {
      correct[i] = (activities[i].consumption_in_wh == maxWh);
    }
  }

  /**
   * getter for the question text
   *
   * @return String - said question
   */
  public String getQuestion() {
    return question;
  }

  /**
   * getter for activity 1
   *
   * @return Activity - the 1st activity
   */
  public Activity getActivity1() {
    return activities[0];
  }

  /**
   * getter for activity 32
   *
   * @return Activity - the 2nd activity
   */
  public Activity getActivity2() {
    return activities[1];
  }

  /**
   * getter for activity 3
   *
   * @return Activity - the 3rd activity
   */
  public Activity getActivity3() {
    return activities[2];
  }

  /**
   * Get the boolean array with correct answers
   *
   * @return the said array
   */
  public boolean[] getCorrectArray() {
    return correct;
  }

}
