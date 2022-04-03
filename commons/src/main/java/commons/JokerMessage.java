package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JokerMessage {
  public Joker joker;
  public String gameSession;
  public String clientId;

  /**
   * Constructor
   *
   * @param joker       joker
   * @param gameSession session
   * @param clientId    client id
   */
  public JokerMessage(@JsonProperty("joker") Joker joker,
                      @JsonProperty("gameSession") String gameSession, @JsonProperty("clientId") String clientId) {
    this.joker = joker;
    this.gameSession = gameSession;
    this.clientId = clientId;
  }
}
