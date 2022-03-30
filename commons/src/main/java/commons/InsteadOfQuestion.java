package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Random;

@SuppressWarnings("unused")
@JsonTypeName("INSTEAD")
public class InsteadOfQuestion extends Question {

  private String title1;
  private String title2;
  private Activity activity1; //X
  private Activity activity2; //Y
  private double factor; //correct answer

  public InsteadOfQuestion() {
    super(Type.INSTEAD);
  }

  public InsteadOfQuestion(Activity activity1, Activity activity2, double factor) {

    super(Type.INSTEAD);
    String titleMock = activity1.getTitle().replace(".", "");
    ;
    titleMock = Character.toLowerCase(titleMock.charAt(0)) + titleMock.substring(1);
    this.title1 = "Instead of " + titleMock + ", ";
    titleMock = activity2.getTitle().replace(".", "");
    titleMock = Character.toLowerCase(titleMock.charAt(0)) + titleMock.substring(1);
    this.title2 = titleMock + " ?";
    this.activity1 = activity1;
    this.activity2 = activity2;
    this.factor = factor;
  }

  /**
   * Returns how close answer is to correct value
   *
   * @param guessedValue value guessed by player
   * @return float between 0 and 1:
   * 0 for more than percentage% away from the correct answer
   * 1 for correct answer
   * linearly between them for answers within the percentage boundary
   */
  public float calculateHowClose(double guessedValue) {
    if (guessedValue < 0) {
      return 0;
    }
    double min = factor * 0.8;
    double max = factor * 1.2;
    if (guessedValue > max || guessedValue < min) {
      return 0;
    } else {
      return 100;
    }
  }


  /**
   * getter for the title 1
   *
   * @return String - Instead of X
   */
  public String getTitle1() {
    return title1;
  }

  /**
   * getter for the title2
   *
   * @return String - how many times could you be doing Y
   */
  public String getTitle2() {
    return title2;
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
   * getter for the factor
   *
   * @return factor  -
   */
  public double getFactor() {
    return factor;
  }

}
