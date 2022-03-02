package server.api;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
  private Map<String, Pair<LocalDateTime, String>> clients = new HashMap<>();

  /**
   * Generates uniqueId for the client when they first connect
   * Put this Id in the map along with the time
   *
   * @return String uniqueId for the client
   */
  @PostMapping("/connect")
  public String addClient(@RequestBody String username) {
    String uniqueID = UUID.randomUUID().toString();
    clients.put(uniqueID, Pair.of(LocalDateTime.now(), username));
    return uniqueID;
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
    Iterator<String> iter = clients.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      LocalDateTime value = clients.get(key).getFirst();
      if (Duration.between(LocalDateTime.now(), value).abs().getSeconds() > 1) {
        clients.remove(key);
        iter = clients.keySet().iterator();
      }
    }
    return clients.size();
  }

  @GetMapping("/players")
  public List<String> getPlayes() {
    List<String> players = new ArrayList<>();
    Iterator<String> iter = clients.keySet().iterator();
    while (iter.hasNext()) {
      players.add(clients.get(iter.next()).getSecond());
    }
    return players;
  }
}
