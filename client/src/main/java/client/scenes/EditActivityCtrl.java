package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditActivityCtrl implements Initializable {
  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  private Activity activity = null;

  @FXML
  private Text activityAuthor;
  @FXML
  private TextField titleField;
  @FXML
  private TextField consumptionField;
  @FXML
  private TextField sourceField;
  @FXML
  private Button saveButton;
  @FXML
  private Button backButton;

  @Inject
  public EditActivityCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  public void back() {
    mainCtrl.showActivityList();
  }

  /**
   * Displays a list of activities from a hashmap.
   *
   * @param activity the activity to edit
   */
  public void refresh(Activity activity) {
    this.activity = activity;
    activityAuthor.setText(activity.author != null ? "Last edited by " + activity.author : "");
    titleField.setText(activity.title);
    consumptionField.setText(Long.toString(activity.consumption_in_wh));
    sourceField.setText(activity.source);

    consumptionField.onMouseClickedProperty().set(event -> {
      consumptionField.getStyleClass().remove("bad");
      saveButton.getStyleClass().remove("bad");
    });
  }

  public void save() {
    if (activity == null) {
      mainCtrl.showActivityList();
      return;
    }
    if (titleField.getText() == null || titleField.getText().trim().equals("")) {
      titleField.getStyleClass().add("bad");
      saveButton.getStyleClass().add("bad");
      return;
    }
    if (consumptionField.getText() == null || consumptionField.getText().trim().equals("")) {
      consumptionField.getStyleClass().add("bad");
      saveButton.getStyleClass().add("bad");
      return;
    }
    if (sourceField.getText() == null || sourceField.getText().trim().equals("")) {
      sourceField.getStyleClass().add("bad");
      saveButton.getStyleClass().add("bad");
      return;
    }

    try {
      server.updateActivity(
        new Activity(
          activity.id,
          mainCtrl.name,
          activity.image_path,
          titleField.getText().trim(),
          Long.parseLong(consumptionField.getText()),
          sourceField.getText().trim()
        )
      );
      mainCtrl.showActivityList();
    } catch (NumberFormatException e) {
      consumptionField.getStyleClass().add("bad");
      saveButton.getStyleClass().add("bad");
    }
  }
}
