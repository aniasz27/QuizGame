package server.api;

import commons.Question;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class GameControllerTest {

  @Autowired
  private GameController gameController;

  @Test
  public void playTest() {
    String gameId = gameController.play(true);
  }
}
