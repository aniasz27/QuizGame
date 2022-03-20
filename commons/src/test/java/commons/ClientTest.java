package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ClientTest {

  static Client client;

  @BeforeAll
  public static void setUp() {
    String id = "ClientID";
    String username = "Username";
    boolean waiting = true;
    client = new Client(id, username, waiting);
  }

  @Test
  public void constructorTest() {
    assertNotNull(client);
  }

  @Test
  public void observeTest() {
    client.observe();
    assertNotNull(client.lastSeen);
  }

  @Test
  public void equalsSame() {
    assertEquals(client, client);
  }

  @Test
  public void equalsSameButDifferent() {
    String id = "ClientID";
    String username = "Username";
    boolean waiting = true;
    Client client2 = new Client(id, username, waiting);
    assertEquals(client, client2);
  }

  @Test
  public void equalsDifferent() {
    String id = "ClientD";
    String username = "Username";
    boolean waiting = true;
    Client client2 = new Client(id, username, waiting);
    assertFalse(client.equals(client2));
  }

  @Test
  public void hashNullTest() {
    client.id = null;
    assertEquals(0, client.hashCode());
    client.id = "ClientID";
  }

  @Test
  public void hashNotNullTest() {
    int hash = client.id.hashCode();
    assertEquals(hash, client.hashCode());
  }
}
