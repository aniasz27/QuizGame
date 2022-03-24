package client.utils;

import client.scenes.MainCtrl;
import commons.EmojiMessage;
import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class EmojiStompSessionHandler implements StompSessionHandler {
  MainCtrl mainCtrl;
  String gameSession;

  EmojiStompSessionHandler(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;
  }

  @Override
  public void afterConnected(
    StompSession session, StompHeaders connectedHeaders) {
    session.subscribe("/queue/emojiChat/" + gameSession, this);
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    System.out.println("Handled framed!");
    EmojiMessage message = (EmojiMessage) payload;
    mainCtrl.showEmoji(message.emoji);
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return EmojiMessage.class;
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                              Throwable exception) {

  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {

  }
}
