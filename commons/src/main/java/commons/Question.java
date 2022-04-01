package commons;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "MULTICHOICE"),
  @JsonSubTypes.Type(value = EstimateQuestion.class, name = "ESTIMATE"),
  @JsonSubTypes.Type(value = HowMuchQuestion.class, name = "HOWMUCH"),
  @JsonSubTypes.Type(value = IntermediateLeaderboardQuestion.class, name = "INTERLEADERBOARD"),
  @JsonSubTypes.Type(value = EndScreen.class, name = "ENDSCREEN"),
  @JsonSubTypes.Type(value = InsteadOfQuestion.class, name = "INSTEAD")
})
@SuppressWarnings("all")
public abstract class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long id;

  public int number;

  // used for displaying the question during the game
  public boolean showCorrect;

  public Type type;

  public enum Type {
    MULTICHOICE(0),
    ESTIMATE(1),
    HOWMUCH(2),
    INTERLEADERBOARD(3),
    ENDSCREEN(4),
    INSTEAD(5);

    private final int type;

    private Type(int t) {
      type = t;
    }

    public boolean equals(Type type) {
      return this.type == type.type;
    }
  }

  public Type getType() {
    return type;
  }


  public Question() {
  }

  public Question(Type type) {
    this.type = type;
  }

  /**
   * Returns how close answer is to correct value
   *
   * @param guessedValue value guessed by player
   * @param correct      value
   * @return float between 0 and 1:
   * 0 for more than percentage% away from the correct answer
   * 1 for correct answer
   * linearly between them for answers within the percentage boundary
   */
  public float calculateHowClose(long guessedValue, long correct) {
    float percentage = 20;
    if (guessedValue < 0 || guessedValue < (100 - percentage) / 100 * correct
      || (100 + percentage) / 100 * correct < guessedValue) {
      return 0;
    }
    return (float) Math.abs(guessedValue - correct) / (correct * -(percentage / 100.0f)) + 1.0f;
  }
}
