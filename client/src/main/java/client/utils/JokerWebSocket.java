package client.utils;

import client.scenes.MainCtrl;

public class JokerWebSocket {
  private MainCtrl mainCtrl;
  private final String gameSession;

  public JokerWebSocket(MainCtrl mainCtrl, String gameSession) {
    this.mainCtrl = mainCtrl;
    this.gameSession = gameSession;

    connectWebSocket();
  }

  private void connectWebSocket() {

  }
}
