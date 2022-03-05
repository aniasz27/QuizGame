package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class HowMuchCtrl {

  private final ServerUtils server;
  private final MainCtrl mainCtrl;

  @FXML
  private Button backButton;
  @FXML
  private Text question;
  @FXML
  private ImageView imageView;
  @FXML
  private Text description;
  @FXML
  private Button answer_1;
  @FXML
  private Button answer_2;
  @FXML
  private Button answer_3;
  @FXML
  private Image image;

  @Inject
  public HowMuchCtrl(ServerUtils server, MainCtrl mainCtrl) {
    this.server = server;
    this.mainCtrl = mainCtrl;
  }

  @FXML
  public void back() {
    mainCtrl.goBackToMenu();
  }

  @FXML
  public void showImage(String imageName) {
    image = new Image(getClass().getResourceAsStream(imageName));
    imageView.setImage(image);
  }
}
