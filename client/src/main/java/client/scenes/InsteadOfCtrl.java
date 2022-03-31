package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.InsteadOfQuestion;
import commons.Question;
import java.io.ByteArrayInputStream;
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

public class InsteadOfCtrl extends QuestionCtrl implements Initializable {

  @FXML
  public StackPane imgContainer1;
  @FXML
  private ImageView imageView1;
  @FXML
  public StackPane imgContainer2;
  @FXML
  private ImageView imageView2;
  @FXML
  private Text title1;
  @FXML
  private Text title2;
  @FXML
  private Text title3;
  @FXML
  private TextField answer;
  @FXML
  private Button submit;
  private int point;
  private InsteadOfQuestion question;
  private boolean dbPoint;
  private Activity activity1;
  private Activity activity2;

  @Inject
  InsteadOfCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  @Override
  public void displayQuestion(Question question) {
    displayEmojis();
    this.question = (InsteadOfQuestion) question;
    this.title1.setText(((InsteadOfQuestion) question).getTitle1());
    this.title2.setText("how many times could you be");
    this.title3.setText(((InsteadOfQuestion) question).getTitle2());
    this.dbPoint = false;
    this.activity1 = ((InsteadOfQuestion) question).getActivity1();
    this.activity2 = ((InsteadOfQuestion) question).getActivity2();
    this.submit.setDisable(false);
    this.answer.getStyleClass().removeAll(Collections.singleton("bad"));
    this.answer.getStyleClass().removeAll(Collections.singleton("good"));
    displayJokers();
    this.hint.setDisable(true);
    showPoints();
    Rectangle clip1 = new Rectangle(
      imgContainer1.getWidth(), imgContainer1.getHeight()
    );
    clip1.setArcWidth(20);
    clip1.setArcHeight(20);
    imgContainer1.setClip(clip1);
    imageView1.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity1.id))));
    Rectangle clip2 = new Rectangle(
      imgContainer2.getWidth(), imgContainer2.getHeight()
    );
    clip2.setArcWidth(20);
    clip2.setArcHeight(20);
    imgContainer2.setClip(clip2);
    imageView2.setImage(new Image(new ByteArrayInputStream(server.getActivityImage(mainCtrl.serverIp, activity2.id))));
    answer.setText("Type in your answer up to 2 decimal places");
  }


  public void showCorrect() {
    answer.getStyleClass().add(point != 0 ? "good" : "bad");
    if (dbPoint) {
      point *= 2;
    }
    mainCtrl.addPoints(point);
    showPoints();
    answer.setText("Correct answer is: " + question.getFactor());
  }

  /**
   * On clicking the submit button on the screen, the answer gets evaluated
   */
  public void checkCorrect() {
    mainCtrl.stopPointsTimer();
    if (answer.getText().equals("")) {
      return;
    }
    double value = Double.parseDouble(answer.getText());
    point = (int) (question.calculateHowClose(value) * mainCtrl.getPointsOffset());
    point = point / 100;
    submit.setDisable(true);
  }

  @Override
  public void disableButtons() {
    super.disableButtons();
    submit.setDisable(true);
    answer.setDisable(true);
  }

  /**
   * Deletes the text upon mouse click
   */
  public void deleteText() {
    answer.setText("");
  }


  public void doublePoints() {
    dbPoint = doublePointsQ();
  }
}

