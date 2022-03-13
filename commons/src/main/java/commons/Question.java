package commons;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "MULTICHOICE"),
  @JsonSubTypes.Type(value = EstimateQuestion.class, name = "ESTIMATE"),
  @JsonSubTypes.Type(value = HowMuchQuestion.class, name = "HOWMUCH")
})
@SuppressWarnings("all")
public abstract class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long id;

  public Type type;


  public enum Type {
    MULTICHOICE(0),
    ESTIMATE(1),
    HOWMUCH(2);

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

}
