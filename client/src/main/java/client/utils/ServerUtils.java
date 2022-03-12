/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import commons.Activity;
import commons.Question;
import commons.Score;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;

public class ServerUtils {

  private static final String SERVER = "http://localhost:8080/";

  private String clientId;
  private String gameId;

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * When user connects first time to the server
   *
   * @return String uniqueId for the client
   */
  public String connectFirst(String username) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/player/connect")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .post(Entity.entity(username, APPLICATION_JSON), String.class);
  }

  /**
   * Client sends http request to the server with their uniqueId
   *
   * @param clientId uniqueId for the client
   * @return String uniqueId for the client
   */
  public String keepAlive(String clientId) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/player/keepAlive")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.entity(clientId, APPLICATION_JSON), String.class);
  }

  /**
   * Returns a game id or null if
   * A game has started for that player or no game started yet respectively
   *
   * @param clientId - unique id of the player
   * @return game id or NULL
   */
  public String isGameActive(String clientId) {
    return ClientBuilder.newClient(new ClientConfig()) //
      .target(SERVER).path("api/game/isGameActive") //
      .request(APPLICATION_JSON) //
      .accept(APPLICATION_JSON) //
      .put(Entity.entity(clientId, APPLICATION_JSON), String.class);
  }

  /**
   * Request to take all players from the waiting room and assign them to a game
   *
   * @return unique generated game id
   */
  public String startGame() {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER)
      .path("api/game/play")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .post(Entity.entity("START GAME", APPLICATION_JSON), String.class);
  }

  /**
   * Request to get a new question from the server
   *
   * @return new Question / null if game ended (after 20 questions)
   */
  public Question nextQuestion() {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER)
      .path("/api/game/next")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get().readEntity(Question.class);

  }


  /**
   * Get all activities from the server
   */
  public List<Activity> getActivities() {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/activity/list")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }

  public Activity updateActivity(Activity activity) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/activity/update")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.entity(activity, APPLICATION_JSON), Activity.class);
  }

  public List<String> getPlayers() {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/player/list")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }

  public String getName(String id) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/player/" + id)
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(String.class);
  }

  public Score addScore(Score score) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/score/add")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.entity(score, APPLICATION_JSON), Score.class);
  }

  public Iterable<Score> getSingleLeaderboard() {
    return ClientBuilder.newClient(new ClientConfig())
      .target(SERVER).path("api/score/leaderboard")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }
}
