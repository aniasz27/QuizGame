package server.api;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
  // maps a unique playerID to the pair of the time when the user was last active and their username
  public Map<String, Pair<LocalDateTime, String>> clients = new HashMap<>();

  /**
   * Generates uniqueId for the client when they first connect
   * Put this Id in the map along with the time
   *
   * @return String uniqueId for the client
   */
  @PostMapping("/connect")
  public ResponseEntity<String> addClient(@RequestBody String username) {
    prunePlayers();
    final boolean[] taken = {false};
    clients.forEach((uuid, userData) -> {
      if (userData.getSecond().equals(username)) {
        taken[0] = true;
      }
    });

    if (taken[0]) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
    }

    String uniqueID = UUID.randomUUID().toString();
    System.out.println("uuid: " + uniqueID + ", username: " + username);
    clients.put(uniqueID, Pair.of(LocalDateTime.now(), username));
    return ResponseEntity.ok(uniqueID);
  }

  /**
   * Update time for the given clientId
   *
   * @param clientId uniqueId for the client
   * @return String uniqueId for the client
   */
  @PutMapping("/keepAlive")
  public String updateClient(@RequestBody String clientId) {
    String username = clients.get(clientId).getSecond();
    clients.put(clientId, Pair.of(LocalDateTime.now(), username));
    return clientId;
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
  public List<String> getPlayers() {
    prunePlayers();
    return clients.values().stream().map(Pair::getSecond).collect(Collectors.toList());
  }

  /**
   * Returns a username based on the id
   *
   * @param id uniqueId for the client
   */
  @GetMapping("/{id}")
  public String getPlayer(@PathVariable("id") String id) {
    return clients.get(id).getSecond();
  }

  private void prunePlayers() {
    clients = clients.entrySet().stream().filter(
      x -> Duration.between(x.getValue().getFirst(), LocalDateTime.now()).getSeconds() <= 1
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
