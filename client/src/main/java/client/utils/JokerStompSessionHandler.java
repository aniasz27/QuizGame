package client.utils;

import client.scenes.MainCtrl;
import commons.Joker;
import commons.JokerMessage;
import java.lang.reflect.Type;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class JokerStompSessionHandler implements StompSessionHandler {
  MainCtrl mainCtrl;
  String gameSession;

  /**
   * Constructor for JokerStompSessionHandler
   *
   * @param mainCtrl    we're using
   * @param gameSession we're in
   */
  JokerStompSessionHandler(MainCtrl mainCtrl, String gameSession) {
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
    session.subscribe("/queue/jokerChat/" + gameSession, this);
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
    JokerMessage message = (JokerMessage) payload;
    Platform.runLater(() -> mainCtrl.showJoker(message.joker, message.clientId));
  }

  /**
   * Returns JokerMessage class
   *
   * @param headers headers
   * @return JokerMessage.class
   */
  @Override
  public Type getPayloadType(StompHeaders headers) {
    return JokerMessage.class;
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
