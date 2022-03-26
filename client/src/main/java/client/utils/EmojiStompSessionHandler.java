package client.utils;

import client.scenes.MainCtrl;
import commons.Emoji;
import commons.EmojiMessage;
import java.lang.reflect.Type;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
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
    System.out.println("Subscribed to: " + gameSession);
    session.subscribe("/queue/emojiChat/" + gameSession, this);
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    System.out.println("Handled framed!");
    Emoji emoji = (Emoji) payload;
    Platform.runLater(() -> mainCtrl.showEmoji(emoji));
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
