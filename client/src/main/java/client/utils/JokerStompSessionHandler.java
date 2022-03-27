package client.utils;

import client.scenes.MainCtrl;
import commons.Joker;
import java.lang.reflect.Type;
import javafx.application.Platform;
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
    System.out.println("Subscribed to: " + gameSession);
    session.subscribe("/queue/jokerChat/" + gameSession, this);
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    System.out.println("Handled framed!");
    Joker joker = (Joker) payload;
    Platform.runLater(() -> mainCtrl.showJoker(joker));
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Joker.class;
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                              Throwable exception) {

  }

  @Override
  public void handleTransportError(StompSession session, Throwable exception) {

  }
}
