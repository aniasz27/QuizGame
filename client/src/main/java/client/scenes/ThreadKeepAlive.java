package client.scenes;

import client.utils.ServerUtils;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadKeepAlive extends Thread {
  private String clientId;
  private final ServerUtils server;

  public ThreadKeepAlive(String clientId, ServerUtils server) {
    this.clientId = clientId;
    this.server = server;
  }

  /**
   * Sends http request from the client to the server every second
   */
  public void run() {
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        server.keepAlive(clientId);
      }
    };
    Timer time = new Timer();
    time.scheduleAtFixedRate(task, 0, 1000);
  }
}
