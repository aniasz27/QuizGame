package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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
  }

  public Question(Type type) {
    this.type = type;
  }
}
