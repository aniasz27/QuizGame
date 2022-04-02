package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ActivityListCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  public List<Activity> activities;

  @FXML
  public TextField searchField;
  @FXML
  private Button helpButton;
  @FXML
  public StackPane searchHelp;
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
    searchField.textProperty().addListener(((observable, oldValue, newValue) -> search(newValue)));
  }

  /**
   * Goes back to the splash screen
   */
  @FXML
  public void back() {
    searchField.setText("");
    mainCtrl.showSplash();
  }

  /**
   * Opens search help
   */
  @FXML
  public void help() {
    searchHelp.setVisible(true);
    searchHelp.setManaged(true);
  }

  /**
   * Closes search help
   */
  @FXML
  public void closeHelp() {
    searchHelp.setVisible(false);
    searchHelp.setManaged(false);
  }

  /**
   * Displays activities matching a query.
   *
   * @param query the conditions an activity has to match to be displayed
   */
  public void search(String query) {
    if (query == null || query.isBlank()) {
      refresh(activities);
      return;
    }

    List<Activity> queryResults = activities;
    // regex found on SO, splits only when not between quotes
    for (String s : query.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
      boolean exclude = s.matches("[!-].*");
      String queryPart = exclude
        ? s.matches("\".*\"") ? s.substring(2, s.length() - 1) : s.substring(1)
        : s.matches("\".*\"") ? s.substring(1, s.length() - 1) : s;

      try {
        if (queryPart.matches("id:.*")) {
          queryResults =
            queryResults.stream().filter(activity -> exclude ^ activity.id.contains(queryPart.substring(3)))
              .collect(Collectors.toList());
        } else if (queryPart.matches("title:.*")) {
          queryResults =
            queryResults.stream().filter(activity -> exclude ^ activity.title.contains(queryPart.substring(6)))
              .collect(Collectors.toList());
        } else if (queryPart.matches("t:.*")) {
          queryResults =
            queryResults.stream().filter(activity -> exclude ^ activity.title.contains(queryPart.substring(2)))
              .collect(Collectors.toList());
        } else if (queryPart.matches("source:.*")) {
          queryResults =
            queryResults.stream().filter(activity -> exclude ^ activity.source.contains(queryPart.substring(7)))
              .collect(Collectors.toList());
        } else if (queryPart.matches("s:.*")) {
          queryResults =
            queryResults.stream().filter(activity -> exclude ^ activity.source.contains(queryPart.substring(2)))
              .collect(Collectors.toList());
        } else if (queryPart.matches("author:.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (
              (queryPart.equals("author:null") && activity.author == null)
                || (activity.author != null && activity.author.contains(queryPart.substring(7)))
            )
          ).collect(Collectors.toList());
        } else if (queryPart.matches("a:.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (
              (queryPart.equals("a:null") && activity.author == null)
                || (activity.author != null && activity.author.contains(queryPart.substring(2)))
            )
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption>.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh > Long.parseLong(queryPart.substring(12)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c>.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh > Long.parseLong(queryPart.substring(2)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption>=.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh >= Long.parseLong(queryPart.substring(13)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c>=.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh >= Long.parseLong(queryPart.substring(3)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption<.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh < Long.parseLong(queryPart.substring(12)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c<.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh < Long.parseLong(queryPart.substring(2)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption<=.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh <= Long.parseLong(queryPart.substring(13)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c<=.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh <= Long.parseLong(queryPart.substring(3)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption=.*") || queryPart.matches("consumption:.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh == Long.parseLong(queryPart.substring(12)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c=.*") || queryPart.matches("c:.*")) {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.consumption_in_wh == Long.parseLong(queryPart.substring(2)))
          ).collect(Collectors.toList());
        } else if (queryPart.matches("consumption~.*")) {
          long value = Long.parseLong(queryPart.substring(12));
          queryResults = queryResults.stream().filter(
            activity -> exclude
              ^ (
              activity.consumption_in_wh < ((value < 20 && value > -20) ? (value + 4) : value * 1.2)
                && activity.consumption_in_wh > ((value < 20 && value > -20) ? (value - 4) : value * 0.8)
            )
          ).collect(Collectors.toList());
        } else if (queryPart.matches("c~.*")) {
          long value = Long.parseLong(queryPart.substring(2));
          queryResults = queryResults.stream().filter(
            activity -> exclude
              ^ (
              activity.consumption_in_wh < ((value < 20 && value > -20) ? (value + 4) : value * 1.2)
                && activity.consumption_in_wh > ((value < 20 && value > -20) ? (value - 4) : value * 0.8)
            )
          ).collect(Collectors.toList());
        } else {
          queryResults = queryResults.stream().filter(
            activity -> exclude ^ (activity.title.contains(queryPart) || activity.id.contains(queryPart))
          ).collect(Collectors.toList());
        }
      } catch (NumberFormatException e) {
        // Ignore, users are dum sometimes
      }
    }

    refresh(queryResults);
  }

  /**
   * Displays activities from a list.
   *
   * @param activityList the list of activities to be displayed
   */
  public void refresh(List<Activity> activityList) {
    activityListDisplay.getChildren().removeAll(activityListDisplay.getChildren());
    if (activityList == null) {
      return;
    }

    int[] i = {0};
    activityList.forEach(activity -> {
      HBox activityBox = new HBox();
      activityBox.getStyleClass().addAll("clickable", "list-item", "border-bottom");
      activityBox.onMouseClickedProperty().set(event -> mainCtrl.showEditActivity(activity));
      if (i[0]++ == 0) {
        activityBox.getStyleClass().add("list-item-top");
      }

      Label l = new Label(activity.title);
      l.getStyleClass().add("expand");
      HBox.setHgrow(l, Priority.ALWAYS);
      activityBox.getChildren().add(l);

      l = new Label("\uF044"); // Edit icon
      l.getStyleClass().addAll("icon", "hover-show");
      l.setMinWidth(Region.USE_PREF_SIZE);
      activityBox.getChildren().add(l);

      activityListDisplay.getChildren().add(activityBox);
    });
  }
}