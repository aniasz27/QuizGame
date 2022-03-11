package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;

@SuppressWarnings("unused")
@JsonTypeName("MULTICHOICE")
public class MultipleChoiceQuestion extends Question {

  private String question;
  private Activity activity1;
  private Activity activity2;
  private Activity activity3;
  private boolean[] correct;

  public MultipleChoiceQuestion() {
    super(Type.MULTICHOICE);
  }

  public MultipleChoiceQuestion(Activity activity1, Activity activity2, Activity activity3) {
    super(Type.MULTICHOICE);
    this.question = "Which of the following activities consumes the most energy?";
    this.activity1 = activity1;
    this.activity2 = activity2;
    this.activity3 = activity3;
    this.correct = new boolean[3];
    computeCorrectAnswer();
  }

  /**
   * Fill the boolean[] correct
   */
  private void computeCorrectAnswer() {
    long maxWh = Math.max(activity1.getConsumption_in_wh(),
      Math.max(activity2.getConsumption_in_wh(),
        activity3.getConsumption_in_wh()));
    correct[0] = activity1.getConsumption_in_wh() == maxWh;
    correct[1] = activity2.getConsumption_in_wh() == maxWh;
    correct[2] = activity3.getConsumption_in_wh() == maxWh;

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
    return activity1;
  }

  /**
   * getter for activity 32
   *
   * @return Activity - the 2nd activity
   */
  public Activity getActivity2() {
    return activity2;
  }

  /**
   * getter for activity 3
   *
   * @return Activity - the 3rd activity
   */
  public Activity getActivity3() {
    return activity3;
  }

  /**
   * Get the boolean array with correct answers
   *
   * @return the said array
   */
  public boolean[] getCorrect() {
    return correct;
  }

}
