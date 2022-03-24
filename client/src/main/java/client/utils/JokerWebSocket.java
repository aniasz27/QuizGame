package client.utils;

import client.scenes.MainCtrl;
import commons.Joker;
import commons.JokerMessage;
import commons.Question;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class JokerWebSocket {
  private MainCtrl mainCtrl;
  private final String gameSession;
  private StompSession session;

  public JokerWebSocket(MainCtrl mainCtrl, String ip, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;

    connectWebSocket();
  }

  private void connectWebSocket() {
    WebSocketClient client = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    JokerStompSessionHandler sessionHandler = new JokerStompSessionHandler(mainCtrl, gameSession);
    String url = mainCtrl.serverIp.replace("http", "ws") + "/websocket";
    try {
      this.session = stompClient.connect(URI.create(url).toString(), sessionHandler).get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      System.err.println("Error creating StompSession");
      e.printStackTrace();
    }
    System.out.println("session + " + session.toString());
  }

  public void sendMessage(Joker joker, Question.Type screen) {
    System.out.println(joker);
    this.session.send("/app/joker", new JokerMessage(screen, joker, gameSession));
  }
}
