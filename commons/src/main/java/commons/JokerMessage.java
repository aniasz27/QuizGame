package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JokerMessage {
  public Question.Type screen;
  public Joker joker;
  public String gameSession;

  public JokerMessage(@JsonProperty("screen") Question.Type screen, @JsonProperty("joker") Joker joker,
                      @JsonProperty("gameSession") String gameSession) {
    this.screen = screen;
    this.joker = joker;
    this.gameSession = gameSession;
  }
}
