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
import commons.Client;
import commons.Question;
import commons.Score;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;

public class ServerUtils {
  /**
   * Initiate connection to the server.
   *
   * @param ip       the ip of the server
   * @param username the chosen username
   * @return the UUID of the client
   */
  public String connect(String ip, String username) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/player/connect").queryParam("username", username)
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .post(null, String.class);
  }

  /**
   * Keep the connection to the server alive.
   *
   * @param ip             the ip of the server
   * @param id             the client's UUID
   * @param waitingForGame whether the client is in the lobby
   * @return String  the client's UUID
   */
  public Client keepAlive(String ip, String id, boolean waitingForGame) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/player/keepAlive").queryParam("id", id)
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.json(waitingForGame), Client.class);
  }

  /**
   * Returns a game id or null if
   * A game has started for that player or no game started yet respectively
   *
   * @param clientId the client's UUID
   * @return null if the client is not in a game, the game's id if they are
   */
  public String isGameActive(String ip, String clientId) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/game/isGameActive")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.json(clientId), String.class);
  }

  /**
   * Request to take all players from the waiting room and assign them to a game
   *
   * @return unique generated game id
   */
  public String startGame(String ip) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip)
      .path("api/game/play")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .post(Entity.json("START GAME"), String.class);
  }

  /**
   * Request to get a new question from the server
   *
   * @return new Question / null if game ended (after 20 questions)
   */
  public Question nextQuestion(String ip) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip)
      .path("/api/game/next")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get().readEntity(Question.class);

  }

  /**
   * Get all activities from the server
   *
   * @param ip the server's IP address
   * @return list of all activities
   */
  public List<Activity> getActivities(String ip) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/activity/list")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }

  /**
   * Get all activities from the server
   *
   * @param ip the server's IP address
   * @param id the ID of the activity
   * @return list of all activities
   */
  public byte[] getActivityImage(String ip, String id) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/activity/image/" + id)
      .request("image/jpeg")
      .get(byte[].class);
  }

  /**
   * Long polling: starts a timer with the server and keeps the connection open
   *
   * @return true if 10s have passed and connection closes, false if an exception has been thrown
   */
  public boolean startServerTimer(String ip, int duration) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip)
      .path("/api/game/finished/" + duration)
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get().readEntity(Boolean.class);
  }

  public Activity updateActivity(String ip, Activity activity) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/activity/update")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .put(Entity.json(activity), Activity.class);
  }

  public List<String> getPlayerNames(String ip) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/player/names")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }

  public Client getClient(String ip, String id) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/player/" + id)
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(Client.class);
  }

  public Score addScore(String ip, Score score) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/score/add")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .post(Entity.json(score), Score.class);
  }

  public Iterable<Score> getSingleLeaderboard(String ip) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/score/leaderboard")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(new GenericType<>() {
      });
  }

  /**
   * Returns a score of a player as an int
   *
   * @param id id of a player
   * @return a score of the player specified by the passed parameter
   */
  public int playerScore(String ip, String id) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/game/" + id + "/score")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(Integer.class);
  }

  /**
   * Updates the score of the player specified by id in the game controller
   *
   * @param id id of a player
   * @return true if the score was updated, false otherwise
   */

  public Score updateScore(String ip, String id, Score score) {
    return ClientBuilder.newClient(new ClientConfig())
      .target(ip).path("api/game/" + id + "/score/update")
      .request(APPLICATION_JSON)
      .accept(APPLICATION_JSON)
      .get(Score.class);
  }


}
