package client.utils;

import client.scenes.MainCtrl;
import commons.Emoji;
import java.net.URI;
import javafx.util.Pair;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class EmojiWebSocket {
  private MainCtrl mainCtrl;
  private String ip;
  private String gameSession;
  /**
   * Most important part of the socket, allows us to send messages.
   */
  private StompSession session;
  private WebSocketStompClient stompClient;

  /**
   * Creates and connects an emoji websocket for a given gameSession
   *
   * @param mainCtrl    scene control object
   * @param ip          of the server
   * @param gameSession of the game to send the emojis through
   */
  public EmojiWebSocket(MainCtrl mainCtrl, String ip, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.ip = ip;
    this.gameSession = gameSession;

    connectWebSocket();
  }

  /**
   * Connects the emoji websocket
   */
  private void connectWebSocket() {
    WebSocketClient client = new StandardWebSocketClient();

    stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    EmojiStompSessionHandler sessionHandler = new EmojiStompSessionHandler(mainCtrl, gameSession);

    stompClient.connect(URI.create("ws://localhost:8080/websocket").toString(), sessionHandler);

    this.session = sessionHandler.session;

    System.out.println("session + " + session.toString());
  }

  /**
   * Wrapper around the session, sends an emoji to the gameSession of the websocket
   *
   * @param emoji being sent
   */
  public void sendMessage(Emoji emoji) {
    this.session.send("/app/emoji", new Pair<Emoji, String>(emoji, gameSession));
  }

}
