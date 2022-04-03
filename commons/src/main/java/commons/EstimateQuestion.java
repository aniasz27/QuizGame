package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonTypeName("ESTIMATE")
public class EstimateQuestion extends Question {

  private String question;
  private Activity activity;

  public EstimateQuestion() {
    super(Type.ESTIMATE);
  }

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
    return calculateHowClose(guessedValue, activity.consumption_in_wh);
  }

  public String getQuestion() {
    return question;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(question, activity);
  }

  @Override
  public String toString() {
    return "EstimateQuestion{"
      + "question='" + question + '\''
      + ", activity=" + activity + '}';
  }
}
