package commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmojiMessage {
  public Emoji emoji;
  public String gameSession;

  /**
   * Constructor for EmojiMessage
   *
   * @param emoji       emoji
   * @param gameSession session
   */
  public EmojiMessage(@JsonProperty("emoji") Emoji emoji, @JsonProperty("gameSession") String gameSession) {
    this.emoji = emoji;
    this.gameSession = gameSession;
  }
}