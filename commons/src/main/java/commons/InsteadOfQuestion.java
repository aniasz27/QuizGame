package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Random;

@SuppressWarnings("unused")
@JsonTypeName("INSTEAD")
public class InsteadOfQuestion extends Question {

  private String question;
  private Activity activity1;
  private Activity activity2;
  private Activity activity3;
  private Activity activity4; //activity we are basing teh question on
  private boolean[] correct;
  private long[] factors;

  public InsteadOfQuestion() {
    super(Type.INSTEAD);
  }

  public InsteadOfQuestion(Activity activity1, Activity activity2, Activity activity3, Activity activity4) {

    super(Type.INSTEAD);
    this.question = "Instead of " + activity4.title + ", you could be:";
    this.activity1 = activity1;
    this.activity2 = activity2;
    this.activity3 = activity3;
    this.activity4 = activity4;
    this.correct = new boolean[3];
    this.factors = new long[3];
    computeCorrectAnswer();
  }

  /**
   * Fill the boolean[] correct + the factors
   */
  private void computeCorrectAnswer() {
    long factor1 = activity4.getConsumption_in_wh() / activity1.getConsumption_in_wh();
    long factor2 = activity4.getConsumption_in_wh() / activity2.getConsumption_in_wh();
    long factor3 = activity4.getConsumption_in_wh() / activity3.getConsumption_in_wh();
    Random random = new Random();
    int correct = random.nextInt(4);
    switch (correct) {
      case 1:
        factor2++;
        factor3--;
        inputValues(true, false, false);
        break;
      case 2:
        factor1++;
        factor3--;
        inputValues(false, true, false);
        break;
      case 3:
        factor1--;
        factor2++;
        inputValues(false, false, true);
        break;
      default:
        System.out.println("Error creating InsteadOf question");
    }
    factors[0] = factor1;
    factors[1] = factor2;
    factors[2] = factor3;
    changeTitles(factors);
  }

  /**
   * Initializes the correct array
   *
   * @param first  true if correct, false otherwise
   * @param second true if correct, false otherwise
   * @param third  true if correct, false otherwise
   */
  public void inputValues(boolean first, boolean second, boolean third) {
    correct[0] = first;
    correct[1] = second;
    correct[2] = third;
  }

  /**
   * Adds factors to the activity titles
   *
   * @param factors factors to be added
   */
  public void changeTitles(long[] factors) {
    activity1.title = activity1.title + " " + factors[0] + " X";
    activity2.title = activity2.title + " " + factors[1] + " X";
    activity3.title = activity3.title + " " + factors[2] + " X";
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
   * getter for activity 4
   *
   * @return Activity - the 4th activity
   */
  public Activity getActivity4() {
    return activity4;
  }

  /**
   * Get the boolean array with correct answers
   *
   * @return the said array
   */
  public boolean[] getCorrect() {
    return correct;
  }

  /**
   * Get the long array with correct factors
   *
   * @return the said array
   */
  public long[] getFactors() {
    return factors;
  }

}
