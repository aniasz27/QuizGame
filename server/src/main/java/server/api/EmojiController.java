package server.api;

import commons.Emoji;
import commons.EmojiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class EmojiController {
  @Autowired
  SimpMessagingTemplate messagingTemplate;

  /**
   * Sends emoji
   *
   * @param emojiMessage containing the emoji
   */
  @MessageMapping("/emoji")
  public void sendEmoji(@Payload EmojiMessage emojiMessage) {
    System.out.println("Sent emoji!");
    Emoji emoji = emojiMessage.emoji;
    String gameSession = emojiMessage.gameSession;

    this.messagingTemplate.convertAndSend("/queue/emojiChat/" + gameSession, emoji);
  }

}