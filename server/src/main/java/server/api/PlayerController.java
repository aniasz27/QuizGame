package server.api;

import commons.Client;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
  private HashMap<String, Client> clients = new HashMap<>();
  // hopefully
  private int anonymousUsers = 0;

  /**
   * Generates a UUID for the client and saves the client
   *
   * @return String uniqueId for the client
   */
  @PostMapping("/connect")
  public ResponseEntity<String> addClient(@RequestParam(required = false) String username) {
    prunePlayers();
    if (username == null) {
      anonymousUsers++;
      username = "Player " + anonymousUsers;
    } else {
      // bullshit to make java happy
      final boolean[] taken = {false};
      String finalUsername = username;

      clients.forEach((id, client) -> {
        if (client.username.equals(finalUsername)) {
          taken[0] = true;
        }
      });

      if (taken[0]) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
      }
    }

    String uuid = UUID.randomUUID().toString();
    System.out.println("uuid: " + uuid + ", username: " + username);
    clients.put(uuid, new Client(uuid, username, false));
    return ResponseEntity.ok(uuid);
  }

  /**
   * Update time for the given clientId
   *
   * @param id      the client's UUID
   * @param waiting whether the client is in the lobby
   * @return String uniqueId for the client
   */
  @PutMapping("/keepAlive")
  public Client keepAlive(@RequestParam String id, @RequestBody boolean waiting) {
    Client client = clients.get(id);
    client.waitingForGame = waiting;
    clients.put(id, client.observe());
    return client;
  }

  /**
   * Iterates over the map and checks which client disconnected
   * Removes the disconnected client from the map
   *
   * @return number of active connections (even after closing the window)
   */
  @GetMapping("/playerCounter")
  public int getPlayerCounter() {
    prunePlayers();
    return clients.size();
  }

  @GetMapping("/list")
  public List<Client> getPlayers() {
    prunePlayers();
    return new ArrayList<>(clients.values());
  }

  @GetMapping("/names")
  public List<String> getPlayerNames() {
    prunePlayers();
    return clients.values().stream().map(client -> client.username).collect(Collectors.toList());
  }

  /**
   * Returns a username based on the id
   *
   * @param id uniqueId for the client
   */
  @GetMapping("/{id}")
  public Client getPlayerById(@PathVariable("id") String id) {
    return clients.get(id);
  }

  private void prunePlayers() {
    clients = (HashMap<String, Client>) clients.entrySet().stream().filter(
      x -> Duration.between(x.getValue().lastSeen, LocalDateTime.now()).getSeconds() <= 1
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
