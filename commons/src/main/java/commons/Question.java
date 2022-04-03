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

    /**
     * Equals method
     *
     * @param type we're comparing with
     * @return true if same, false otherwise
     */
    public boolean equals(Type type) {
      return this.type == type.type;
    }
  }

  /**
   * Getter for type
   *
   * @return type
   */
  public Type getType() {
    return type;
  }

  /**
   * Empty Question Constructor
   */
  public Question() {
  }

  /**
   * Type constructor
   *
   * @param type type of question
   */
  public Question(Type type) {
    this.type = type;
  }

  /**
   * Returns how close answer is to correct value
   *
   * @param guessedValue value guessed by player
   * @param correct      value
   * @return int points to be awarded for the response
   * 0 for more than percentage% away from the correct answer
   * value between 100 and 75 otherwise
   * linearly between them for answers within the percentage boundary
   */
  public int calculateHowClose(long guessedValue, long correct) {
    float percentage = 20;
    if (guessedValue < 0 || guessedValue < (1 - (percentage / 100)) * correct
      || guessedValue > (1 + (percentage / 100)) * correct) {
      return 0;
    }
    double ratio = (double) guessedValue / correct;
    if (guessedValue == correct) {
      return 100;
    } else if (guessedValue < correct) {
      return (int) (ratio * 125 - 25);
    } else {
      return (int) (ratio * -125 + 225);
    }
  }
}
