package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void emptyConstructorTest() {
    Game gameEmpty = new Game();
    assertNotNull(gameEmpty);
  }


  @Test
  public void gameIdConstructorTest() {
    String id = "ID";
    Game gameWithId = new Game(id, true);
    assertNotNull(gameWithId);
  }

  @Test
  public void gameConstructorFullTest1() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    List<Client> collection = new LinkedList<>();
    collection.add(c1);
    collection.add(c2);
    collection.add(c3);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    List<Question> collectionQuestions = new LinkedList<>();
    collectionQuestions.add(q1);
    collectionQuestions.add(q2);
    collectionQuestions.add(q3);
    String id = "ID";
    Game game = new Game(id, collection, collectionQuestions, true);
    assertNotNull(game);
  }

  @Test
  public void gameConstructorFullTest2() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    List<Client> collection = new LinkedList<>();
    collection.add(c1);
    collection.add(c2);
    collection.add(c3);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    assertNotNull(game);
  }

  @Test
  public void gameConstructorFullTest3() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    collection.put(c2, 0);
    collection.put(c3, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    List<Question> collectionQuestions = new LinkedList<>();
    collectionQuestions.add(q1);
    collectionQuestions.add(q2);
    collectionQuestions.add(q3);
    String id = "ID";
    Game game = new Game(id, collection, collectionQuestions, true);
    assertNotNull(game);
  }

  @Test
  public void gameConstructorFullTest4() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    collection.put(c2, 0);
    collection.put(c3, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    assertNotNull(game);
  }

  @Test
  public void addPlayerTest() {
    String id = "GameID";
    Client c1 = new Client("ID1", "US1", true);
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    game.addPlayer(c1);
    assertTrue(game.players.size() > 0);
  }

  @Test
  public void changeScoreTest() {
    String id = "GameID";
    Client c1 = new Client("ID1", "US1", true);
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    game.addPlayer(c1);
    game.changeScore(c1, 100);
    assertEquals(100, (int) game.players.get(c1));
  }

  @Test
  public void increaseScoreTest() {
    String id = "GameID";
    Client c1 = new Client("ID1", "US1", true);
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    game.addPlayer(c1);
    game.increaseScore(c1, 50);
    assertEquals(50, (int) game.players.get(c1));
  }

  @Test
  public void getByIdTest1() {
    String id = "GameID";
    Client c1 = new Client("ID1", "US1", true);
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    game.addPlayer(c1);
    assertEquals(c1, game.getPlayerById("ID1"));
  }

  @Test
  public void getByIDNull() {
    String id = "GameID";
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    assertNull(game.getPlayerById("ID1"));
  }

  @Test
  public void containsPlayerTest() {
    String id = "GameID";
    Client c1 = new Client("ID1", "US1", true);
    Map<Client, Integer> map = new HashMap<>();
    Question[] questions = new Question[3];
    Game game = new Game(id, map, questions, true);
    game.addPlayer(c1);
    assertTrue(game.containsPlayer("ID1"));
  }

  @Test
  public void nextQuestionTest() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    collection.put(c2, 0);
    collection.put(c3, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    assertEquals(q1, game.current(true));
  }

  @Test
  public void nextQuestionEndTest() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    Question q1 = new EstimateQuestion();
    Question[] questions = new Question[22];

    for (int i = 0; i < 21; i++) {
      questions[i] = q1;
      questions[i].number = i;
    }
    questions[10] = new IntermediateLeaderboardQuestion();
    questions[10].number = 10;
    questions[21] = new EndScreen();
    questions[21].number = 21;
    String id = "ID";
    Game game = new Game(id, collection, questions, false);
    Game game2 = new Game(id, collection, questions, true);


    Question finalQuestion = null;
    for (int i = 0; i < 21; i++) {
      finalQuestion = game.current(true);
      game.increaseQuestionCounter();
      game2.increaseQuestionCounter();
    }
    game.increaseQuestionCounter();
    assertTrue(finalQuestion.getType().equals(Question.Type.ENDSCREEN));
    finalQuestion = game2.current(true);
    assertTrue(finalQuestion.getType().equals(Question.Type.ENDSCREEN));
  }

  @Test
  public void currentTest() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question[] questions = new Question[22];
    questions[0] = q1;
    for (int i = 1; i < 22; i++) {
      questions[i] = q2;
    }
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    assertEquals(q1, game.current(false));
  }

  @Test
  public void toStringTest() {
    Game game = new Game();
    String result = "Game{id='null', players={}, questionCounter=0, questions=null}";
    assertEquals(result, game.toString());
  }

  @Test
  public void equalsSame() {
    Game game = new Game();
    assertEquals(game, game);
  }

  @Test
  public void equalsNull() {
    assertNotEquals(new Game(), null);
  }

  @Test
  public void equalsNotGame() {
    assertNotEquals(new Game(), new Score("ClientID", "Name", 10));
  }

  @Test
  public void equalsSameButDifferent() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    collection.put(c2, 0);
    collection.put(c3, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    Game gameNew = new Game(id, collection, questions, true);
    assertEquals(game, gameNew);
  }

  @Test
  public void equalsDifferent() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    collection.put(c2, 0);
    collection.put(c3, 0);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Map<Client, Integer> collection1 = new HashMap<>();
    Game game = new Game(id, collection, questions, true);
    Game gameNew = new Game(id, collection1, questions, true);
    assertNotEquals(game, gameNew);
  }

  @Test
  public void hashTest() {
    String id = "ID";
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    Map<Client, Integer> collection = new HashMap<>();
    collection.put(c1, 0);
    Question q1 = new EstimateQuestion();
    Question[] questions = new Question[22];
    for (int i = 0; i < 22; i++) {
      questions[i] = q1;
    }
    Game game = new Game(id, collection, questions, true);
    assertEquals(game.hashCode(), Math.floor(game.hashCode()));
  }

  @Test
  void multiplayerBooleanTest() {
    Client c1 = new Client("ID1", "US1", true);
    Client c2 = new Client("ID2", "US2", true);
    Client c3 = new Client("ID3", "US3", true);
    List<Client> collection = new LinkedList<>();
    collection.add(c1);
    collection.add(c2);
    collection.add(c3);
    Question q1 = new EstimateQuestion();
    Question q2 = new HowMuchQuestion();
    Question q3 = new MultipleChoiceQuestion();
    Question[] questions = new Question[3];
    questions[0] = q1;
    questions[1] = q2;
    questions[2] = q3;
    String id = "ID";
    Game game = new Game(id, collection, questions, true);
    assertTrue(game.isMultiplayer());
    game.setMultiplayer(false);
    assertFalse(game.isMultiplayer());
  }

  @Test
  public void getPlayers() {
    assertEquals(
      Map.of(
        new Client("ID1", "US1", true),
        0,
        new Client("ID2", "US2", true),
        0,
        new Client("ID3", "US3", true),
        0),
      new Game(
        "ID",
        List.of(
          new Client("ID1", "US1", true),
          new Client("ID2", "US2", true),
          new Client("ID3", "US3", true)
        ),
        new Question[] {
          new EstimateQuestion(),
          new HowMuchQuestion(),
          new MultipleChoiceQuestion()
        }, true
      ).getPlayers()
    );
  }
}