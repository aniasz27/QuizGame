package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ENDSCREEN")
public class EndScreen extends Question {
  /**
   * Constructor for EndScreen
   */
  public EndScreen() {
    super(Type.ENDSCREEN);
  }
}
