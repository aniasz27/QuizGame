package client.scenes;

import client.scenes.helpers.QuestionCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.EstimateQuestion;
import commons.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class GuessCtrl extends QuestionCtrl implements Initializable {
  private EstimateQuestion question;
  private Activity activity;

  @FXML
  private ImageView imageView;
  @FXML
  private Text description;
  @FXML
  private TextField answer;

  @Inject
  GuessCtrl(ServerUtils server, MainCtrl mainCtrl) {
    super(server, mainCtrl);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  @FXML
  private void submit(ActionEvent actionEvent) {
    //TODO
    //validate the answer
  }

  @Override
  public void displayQuestion(Question question) {
    this.question = (EstimateQuestion) question;
    this.activity = this.question.getActivity();
    imageView = new ImageView(getClass().getResource(activity.getImage_path()).toExternalForm());
    description.setText(activity.getTitle());
  }

  public void checkCorrect() {
    long value = Long.parseLong(answer.getText());
    long point = (long) (question.calculateHowClose(value) * 100);
    mainCtrl.addPoints(point);
  }
}