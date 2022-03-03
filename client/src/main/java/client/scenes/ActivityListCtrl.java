package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ActivityListCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private VBox activityListDisplay;
  @FXML
  private Button backButton;

  @Inject
  public ActivityListCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void back() {
    mainCtrl.showSplash();
  }

  /**
   * Displays a list of activities from a hashmap.
   *
   * @param activities the map of activities to create the list from
   */
  public void refresh(List<String> activities) {
    // TODO: get list of activities from server and display it
    //var quotes = server.getPlayers();
    //data = FXCollections.observableList(quotes);
    //table.setItems(data);
    if (activities == null) {
      mainCtrl.showConnect();
      return;
    }

    // remove all activities and re-add them
    activityListDisplay.getChildren().removeAll(activityListDisplay.getChildren());
    int[] i = {0};
    activities.forEach(a -> {
      HBox activity = new HBox();
      Label l = new Label(a);
      l.getStyleClass().add("expand");
      HBox.setHgrow(l, Priority.ALWAYS);
      activity.getChildren().add(l);
      l = new Label("Edit");
      l.getStyleClass().add("hover-show");
      activity.getChildren().add(l);
      activity.getStyleClass().add("clickable");
      activity.getStyleClass().add("list-item");
      activity.getStyleClass().add("border-bottom");
      if (i[0]++ == 0) {
        activity.getStyleClass().add("list-item-top");
      }
      activityListDisplay.getChildren().add(activity);
    });
  }
}