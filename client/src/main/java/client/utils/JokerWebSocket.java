package client.utils;

import client.scenes.MainCtrl;
import commons.Joker;
import commons.JokerMessage;
import java.net.URI;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class JokerWebSocket {
  private MainCtrl mainCtrl;
  private final String gameSession;
  private StompSession session;

  /**
   * Creates and connects a joker websocket for a given gameSession
   *
   * @param mainCtrl    scene control object
   * @param gameSession of the game to send the emojis through
   */
  public JokerWebSocket(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;

    connectWebSocket();
  }

  /**
   * Connects the joker websocket to the server
   */
  private void connectWebSocket() {
    WebSocketClient client = new StandardWebSocketClient();

    WebSocketStompClient stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    JokerStompSessionHandler sessionHandler = new JokerStompSessionHandler(mainCtrl, gameSession);

    String url = mainCtrl.serverIp.replace("http", "ws") + "/websocket";
    try {
      this.session =
        stompClient.connect(URI.create(url).toString(), sessionHandler).get();
    } catch (Exception e) {
      System.err.println("Error creating stompSession");
      e.printStackTrace();
    }

    System.out.println("session + " + session.toString());
  }

  /**
   * Wrapper around the session, sends a joker to the gameSession of the websocket
   *
   * @param joker being sent
   */
  public void sendMessage(Joker joker) {
    System.out.println(joker);
    this.session.send("/app/joker", new JokerMessage(joker, gameSession, mainCtrl.clientId));
  }
}
