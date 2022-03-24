package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.scenes.MainCtrl;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

public class GameUtils {

  MainCtrl mainCtrl;

  @Inject
  public GameUtils(MainCtrl mainCtrl) {
    this.mainCtrl = mainCtrl;
  }

  /**
   * Returns a game id or null if
   * A game has started for that player or no game started yet respectively
   *
   * @param clientId the client's UUID
   */
  public void isActive(String ip, String clientId) {
    new Thread(() -> {
      while (true) {
        String gameId = ClientBuilder.newClient(new ClientConfig())
          .target(ip).path("api/game/isActive").queryParam("uid", clientId)
          .request(APPLICATION_JSON)
          .accept(APPLICATION_JSON)
          .put(Entity.json(clientId), String.class);

        if (mainCtrl.waitingForGame && gameId != null && !gameId.isBlank()) {
          mainCtrl.gameId = gameId;
          mainCtrl.waitingForGame = false;
          mainCtrl.start(); // absolutely critical that this is .start()
          break;
        }
      }
    }).start();
  }
}
