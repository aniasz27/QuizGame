package client.utils;

import client.scenes.MainCtrl;
import commons.JokerMessage;
import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class JokerStompSessionHandler implements StompSessionHandler {
  MainCtrl mainCtrl;
  String gameSession;

  JokerStompSessionHandler(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;
  }

  @Override
  public void afterConnected(
    StompSession session, StompHeaders connectedHeaders) {
    session.subscribe("/queue/" + gameSession, this);
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    System.out.println("Handled framed!");
    JokerMessage message = (JokerMessage) payload;
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return JokerMessage.class;
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                              Throwable exception) {

  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {

  }
}
