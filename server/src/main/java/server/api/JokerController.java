package server.api;

import commons.JokerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class JokerController {
  @Autowired
  SimpMessagingTemplate messagingTemplate;

  /**
   * Sends a joker
   *
   * @param jokerMessage joker will be extracted from
   */
  @MessageMapping("/joker")
  public void sendJoker(@Payload JokerMessage jokerMessage) {
    System.out.println("Sent joker");
    this.messagingTemplate.convertAndSend("/queue/jokerChat/" + jokerMessage.gameSession, jokerMessage);
  }
}
