package client.utils;

import client.scenes.MainCtrl;
import commons.Emoji;
import java.lang.reflect.Type;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class EmojiStompSessionHandler implements StompSessionHandler {
  MainCtrl mainCtrl;
  String gameSession;

  /**
   * Constructor for EmojiStompSessionHandler
   *
   * @param mainCtrl    we're using
   * @param gameSession we're in
   */
  EmojiStompSessionHandler(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;
  }

  /**
   * Subscribes to the session
   *
   * @param session          we're subscribed to
   * @param connectedHeaders handler
   */
  @Override
  public void afterConnected(
    StompSession session, StompHeaders connectedHeaders) {
    System.out.println("Subscribed to: " + gameSession);
    session.subscribe("/queue/emojiChat/" + gameSession, this);
  }

  /**
   * Handles the frame
   *
   * @param headers headers
   * @param payload payload
   */
  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    System.out.println("Handled framed!");
    Emoji emoji = (Emoji) payload;
    Platform.runLater(() -> mainCtrl.showEmoji(emoji));
  }

  /**
   * Returns the Emoji class
   *
   * @param headers headers
   * @return Emoji.class
   */
  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Emoji.class;
  }

  /**
   * Exception Handling
   *
   * @param session   session
   * @param command   command
   * @param headers   headers
   * @param payload   payload
   * @param exception exception
   */
  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                              Throwable exception) {

  }

  /**
   * TransportError handling
   *
   * @param session   session
   * @param exception exception
   */
  @Override
  public void handleTransportError(StompSession session, Throwable exception) {

  }
}
