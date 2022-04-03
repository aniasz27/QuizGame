package client.utils;

import client.scenes.MainCtrl;
import commons.Emoji;
import commons.EmojiMessage;
import java.net.URI;
import java.util.Objects;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class EmojiWebSocket {
  private MainCtrl mainCtrl;
  private final String gameSession;
  /**
   * Most important part of the socket, allows us to send messages.
   */
  private StompSession session;

  /**
   * Creates and connects an emoji websocket for a given gameSession
   *
   * @param mainCtrl    scene control object
   * @param gameSession of the game to send the emojis through
   */
  public EmojiWebSocket(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;

    try {
      connectWebSocket();
    } catch (Exception e) {
      System.err.println("Error connecting Emoji WebSocket");
    }
  }

  /**
   * Connects the emoji websocket to the server
   */
  private void connectWebSocket() {
    WebSocketClient client = new StandardWebSocketClient();

    WebSocketStompClient stompClient = new WebSocketStompClient(client);
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    EmojiStompSessionHandler sessionHandler = new EmojiStompSessionHandler(mainCtrl, gameSession);

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
   * Wrapper around the session, sends an emoji to the gameSession of the websocket
   *
   * @param emoji being sent
   */
  public void sendMessage(Emoji emoji) {
    System.out.println(emoji);
    this.session.send("/app/emoji", new EmojiMessage(emoji, gameSession));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmojiWebSocket that = (EmojiWebSocket) o;
    return Objects.equals(mainCtrl, that.mainCtrl)
      && Objects.equals(gameSession, that.gameSession)
      && Objects.equals(session, that.session);
  }
}
