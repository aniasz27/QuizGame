package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ENDSCREEN")
public class EndScreen extends Question {
  public EndScreen() {
    super(Type.ENDSCREEN);
  }
}
