package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmojiMessage {
  public Emoji emoji;
  public String gameSession;

  public EmojiMessage(@JsonProperty("emoji") Emoji emoji, @JsonProperty("gameSession") String gameSession) {
    this.emoji = emoji;
    this.gameSession = gameSession;
  }
}