package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.scenes.MainCtrl;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javafx.application.Platform;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

public class GameUtils {

  MainCtrl mainCtrl;

  /**
   * Constructor for GameUtils
   *
   * @param mainCtrl we're using
   */
  @Inject
  public GameUtils(MainCtrl mainCtrl) {
    this.mainCtrl = mainCtrl;
  }


  private static ScheduledExecutorService EXECisActive;

  /**
   * Returns a game id or null if
   * A game has started for that player or no game started yet respectively
   *
   * @param clientId the client's UUID
   */
  public void isActive(String ip, String clientId) {
    EXECisActive = Executors.newSingleThreadScheduledExecutor();
    try {
      EXECisActive.submit(() -> {
        while (!EXECisActive.isShutdown()) {
          String gameId = ClientBuilder.newClient(new ClientConfig())
            .target(ip).path("api/game/isActive").queryParam("uid", clientId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.json(clientId), String.class);

          if (mainCtrl.waitingForGame && gameId != null && !gameId.isBlank()) {
            mainCtrl.gameId = gameId;
            mainCtrl.waitingForGame = false;
            Platform.runLater(() -> mainCtrl.play());
            EXECisActive.shutdownNow();
            break;
          }
        }
      });
    } catch (Exception e) {
      if (!EXECisActive.isShutdown()) {
        isActive(ip, clientId);
      }
    }
  }

  /**
   * Stops the thread which asks if game is active
   */
  public void stopIsActiveThread() {
    EXECisActive.shutdownNow();
  }
}
