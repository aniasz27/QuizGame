package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class WaitingRoomCtrl implements Initializable {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @Inject
  public WaitingRoomCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void refresh() {
    // TODO: get list of players from server and display it
    //var quotes = server.getPlayers();
    //data = FXCollections.observableList(quotes);
    //table.setItems(data);
  }
}
