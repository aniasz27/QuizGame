package client.utils;

import client.scenes.MainCtrl;
import commons.Emoji;
import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class EmojiStompSessionHandler implements StompSessionHandler {
  MainCtrl mainCtrl;
  String gameSession;
  public StompSession session;

  EmojiStompSessionHandler(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;
  }

  @Override
  public void afterConnected(
    StompSession session, StompHeaders connectedHeaders) {
    session.subscribe("/queue/emojiChat/" + gameSession, this);
    this.session = session;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    Emoji emoji = (Emoji) payload;
    mainCtrl.showEmoji(emoji);
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Emoji.class;
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                              Throwable exception) {

  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {

  }
}
