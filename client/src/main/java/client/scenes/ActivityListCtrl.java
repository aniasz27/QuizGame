package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
   * Displays activities retrieved from the server.
   */
  public void refresh() {
    List<Activity> activities = server.getActivities(mainCtrl.serverIp);
    activityListDisplay.getChildren().removeAll(activityListDisplay.getChildren());
    if (activities == null) {
      System.out.println("uh oh");
      return;
    }

    int[] i = {0};
    activities.forEach(a -> {
      HBox activity = new HBox();
      Label l = new Label(a.title);
      l.getStyleClass().add("expand");
      HBox.setHgrow(l, Priority.ALWAYS);
      activity.getChildren().add(l);
      l = new Label("\uF044"); // Edit icon
      l.getStyleClass().add("icon");
      l.getStyleClass().add("hover-show");
      l.setMinWidth(Region.USE_PREF_SIZE);
      activity.getChildren().add(l);
      activity.getStyleClass().add("clickable");
      activity.getStyleClass().add("list-item");
      activity.getStyleClass().add("border-bottom");
      activity.onMouseClickedProperty().set(event -> mainCtrl.showEditActivity(a));
      if (i[0]++ == 0) {
        activity.getStyleClass().add("list-item-top");
      }
      activityListDisplay.getChildren().add(activity);
    });
  }
}