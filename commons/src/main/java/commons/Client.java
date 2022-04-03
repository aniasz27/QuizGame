package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Objects;

public class Client {
  public String id;
  public String username;
  // jackson doesn't know how to serialize this, so don't
  @JsonIgnore
  public LocalDateTime lastSeen = LocalDateTime.now();
  public boolean waitingForGame;

  /**
   * Empty Constructor for Client
   */
  public Client() {
  }

  /**
   * Client Constructor
   *
   * @param id             client id
   * @param username       client username
   * @param waitingForGame true if waiting, false otherwise
   */
  public Client(String id, String username, boolean waitingForGame) {
    this.id = id;
    this.username = username;
    this.waitingForGame = waitingForGame;
  }

  /**
   * Initializes lastSeen
   *
   * @return this Client
   */
  public Client observe() {
    lastSeen = LocalDateTime.now();
    return this;
  }

  /**
   * Equals method for Client
   *
   * @param o object we're comparing to
   * @return true if equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Client client = (Client) o;

    return Objects.equals(id, client.id) && Objects.equals(username, client.username)
      && client.waitingForGame == waitingForGame;
  }

  /**
   * Hash function for Client
   *
   * @return hashcode
   */
  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

}
