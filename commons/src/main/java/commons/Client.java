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

  public Client() {
  }

  public Client(String id, String username, boolean waitingForGame) {
    this.id = id;
    this.username = username;
    this.waitingForGame = waitingForGame;
  }

  public Client observe() {
    lastSeen = LocalDateTime.now();
    return this;
  }

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

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  // i'm leaving this in bc it's kinda funny and gitinspector doesn't count comments anyway
  // this is really stupid hence the stupid name
  // i dare yall to try saying this really fast even once
  // Oxford Dictionary:
  // Serializablize
  // 1. to make serializable
  //public SerializableClient serializablize() {
  //  return new SerializableClient(id, username, waiting);
  //}
}
