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
}
