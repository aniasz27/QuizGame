package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonTypeName("ESTIMATE")
public class EstimateQuestion extends Question {

  private String question;
  private Activity activity;

  /**
   * Empty constructor for EstimateQuestion
   */
  public EstimateQuestion() {
    super(Type.ESTIMATE);
  }

  /**
   * Constructor for EstimateQuestion
   *
   * @param activity we're basing the question on
   */
  public EstimateQuestion(Activity activity) {
    super(Type.ESTIMATE);
    this.question = "How much does this activity take?";
    this.activity = activity;
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
  public float calculateHowClose(long guessedValue) {
    float percentage = 20;
    long correct = getActivity().consumption_in_wh;
    if (guessedValue < (100 - percentage) / 100 * correct || (100 + percentage) / 100 * correct < guessedValue) {
      return 0;
    }
    return (float) Math.abs(guessedValue - correct) / (correct * -(percentage / 100.0f)) + 1.0f;
  }

  /**
   * Getter for question
   *
   * @return question
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Getter for activity
   *
   * @return activity
   */
  public Activity getActivity() {
    return activity;
  }

  /**
   * @return the correct answer to the question
   */
  @JsonIgnore
  public long getAnswer() {
    return getActivity().consumption_in_wh;
  }

  /**
   * Equals method
   *
   * @param o object we're comparing to
   * @return true if equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EstimateQuestion)) {
      return false;
    }
    EstimateQuestion that = (EstimateQuestion) o;
    return question.equals(that.question) && activity.equals(that.activity);
  }

  /**
   * Hash function
   *
   * @return hashcode
   */

  @Override
  public int hashCode() {
    return Objects.hash(question, activity);
  }

  /**
   * Textual representation of EstimateQuestion
   *
   * @return String description of EstimateQuestion
   */

  @Override
  public String toString() {
    return "EstimateQuestion{"
      + "question='" + question + '\''
      + ", activity=" + activity + '}';
  }
}
