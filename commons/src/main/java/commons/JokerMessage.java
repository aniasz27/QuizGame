package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JokerMessage {
  public Joker joker;
  public String gameSession;

  public JokerMessage(@JsonProperty("joker") Joker joker,
                      @JsonProperty("gameSession") String gameSession) {
    this.joker = joker;
    this.gameSession = gameSession;
  }
}
