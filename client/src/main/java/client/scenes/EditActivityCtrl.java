package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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
  public StackPane imgContainer;
  @FXML
  private ImageView imageView;
  @FXML
  private Button imgButton;
  @FXML
  private Button saveButton;
  @FXML
  private Button backButton;

  FileChooser fileChooser = new FileChooser();

  @Inject
  public EditActivityCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("Images", "*.jpg")
    );
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

    Rectangle clip = new Rectangle(
      imgContainer.getWidth(), imgContainer.getHeight()
    );
    clip.setArcWidth(20);
    clip.setArcHeight(20);
    imgContainer.setClip(clip);
    imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));

    consumptionField.onMouseClickedProperty().set(event -> {
      consumptionField.getStyleClass().removeAll(Collections.singleton("bad"));
      saveButton.getStyleClass().removeAll(Collections.singleton("bad"));
    });
  }

  @FXML
  public void back() {
    mainCtrl.showActivityList();
  }

  @FXML
  public void changeImage() {
    File file = fileChooser.showOpenDialog(mainCtrl.primaryStage);

    try {
      byte[] bytes = new byte[(int) file.length()];
      try (FileInputStream fis = new FileInputStream(file)) {
        fis.read(bytes);
      } catch (IOException e) {
        e.printStackTrace();
      }

      server.changeActivityImage(mainCtrl.serverIp, activity.id, bytes);

      imageView.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity.id))));
    } catch (NullPointerException e) {
      // Ignore, user didn't choose an image
    }
  }

  @FXML
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
        mainCtrl.serverIp,
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
