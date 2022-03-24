package server.api;

import commons.Emoji;
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

  @MessageMapping("/emoji")
  public void sendEmoji(@Payload Pair<Emoji, String> emojiSessionPair) throws Exception {
    Emoji emoji = emojiSessionPair.getFirst();
    String gameSession = emojiSessionPair.getSecond();

    this.messagingTemplate.convertAndSend("/queue/emojiChat/" + gameSession, emoji);
  }

}