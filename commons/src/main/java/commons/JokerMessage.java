package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JokerMessage {
  public String gameSession;

  public JokerMessage(@JsonProperty("gameSession") String gameSession) {
    this.gameSession = gameSession;
  }
}
