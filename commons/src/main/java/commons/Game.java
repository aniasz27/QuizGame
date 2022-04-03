package commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;

public class Game {
  public String id;
  public Map<Client, Integer> players = new HashMap<>();
  public int questionCounter = 0;
  public Question[] questions;
  public boolean multiplayer = false;
  public Map<Object, Consumer<Question>> playerListeners = new HashMap<>();
  public ScheduledExecutorService execTiming;

  @SuppressWarnings("unused")
  public Game() {
  }

  /**
   * Creates a new game without players
   *
   * @param id          the game's ID
   * @param multiplayer the game type
   */
  public Game(String id, boolean multiplayer) {
    this.id = id;
    this.multiplayer = multiplayer;
  }

  /**
   * Creates a new game with players from a collection with scores set to 0
   *
   * @param id          the game's ID
   * @param players     the players in the game
   * @param questions   the questions in this game
   * @param multiplayer the game type
   */
  public Game(String id, Collection<Client> players, Collection<Question> questions, boolean multiplayer) {
    this.id = id;
    this.players = players.stream().collect(Collectors.toMap(client -> client, client -> 0));
    this.questions = questions.toArray(new Question[22]);
    this.multiplayer = multiplayer;
  }

  /**
   * Creates a new game with players from a collection with scores set to 0
   *
   * @param id          the game's ID
   * @param players     the players in the game
   * @param questions   the questions in this game
   * @param multiplayer the game type
   * @param multiplayer the game type
   */
  public Game(String id, Collection<Client> players, Question[] questions, boolean multiplayer) {
    this.id = id;
    this.players = players.stream().collect(Collectors.toMap(client -> client, client -> 0));
    this.questions = questions;
    this.multiplayer = multiplayer;
  }

  /**
   * Creates a new game with players from a map
   *
   * @param id          the game's ID
   * @param players     map of players and their scores
   * @param questions   the questions in this game
   * @param multiplayer the game type
   */
  public Game(String id, Map<Client, Integer> players, Collection<Question> questions, boolean multiplayer) {
    this.id = id;
    this.players = players;
    this.questions = questions.toArray(new Question[22]);
    this.multiplayer = multiplayer;
  }

  /**
   * Creates a new game with players from a map
   *
   * @param id          the game's ID
   * @param players     map of players and their scores
   * @param questions   the questions in this game
   * @param multiplayer the game type
   */
  public Game(String id, Map<Client, Integer> players, Question[] questions, boolean multiplayer) {
    this.id = id;
    this.players = players;
    this.questions = questions;
    this.multiplayer = multiplayer;
  }

  /**
   * Adds a new player to the game
   *
   * @param player the player to be added to this game
   */
  public void addPlayer(Client player) {
    players.put(player, 0);
  }

  /**
   * Updates a player's score
   *
   * @param player the player to be updated
   * @param score  the player's new score
   * @return the player's new score
   */
  public int changeScore(Client player, int score) {
    return players.replace(player, score);
  }

  /**
   * Increases a player's score
   *
   * @param player the player to be updated
   * @param amount the amount to be added to the player's score
   * @return the player's new score
   */
  public int increaseScore(Client player, int amount) {
    return players.replace(player, players.get(player) + amount);
  }

  /**
   * Gets a player by their ID if they are in this game, null otherwise
   *
   * @param userId the ID of the player to get
   * @return the player if they are in this game, null otherwise
   */
  public Client getPlayerById(String userId) {
    return players.keySet().stream().filter(p -> p.id.equals(userId)).findFirst().orElse(null);
  }

  /**
   * Sets the multiplayer field
   *
   * @param state - true if singleplayer, false if multiplayer
   */
  public void setMultiplayer(boolean state) {
    this.multiplayer = state;
  }

  /**
   * A getter for the multiplayer field
   *
   * @return true if player is in multiplayer game, false otherwise
   */
  public boolean isMultiplayer() {
    return this.multiplayer;
  }


  /**
   * Checks if a player with a given ID is in this game
   *
   * @param userId the ID of the player
   * @return true if the player in this game, false otherwise
   */
  public boolean containsPlayer(String userId) {
    return players.keySet().stream().anyMatch(p -> p.id.equals(userId));
  }

  /**
   * Gets the next question.
   *
   * @return the next question
   */
  public void increaseQuestionCounter() {
    questionCounter++;
  }

  /**
   * Gets the next question.
   *
   * @return the next question
   */
  public Question current(Boolean showCorrect) {
    Question question;
    question = questions[questionCounter];
    question.showCorrect = showCorrect;
    if (question.type.equals(Question.Type.INTERLEADERBOARD) && !isMultiplayer()) {
      questionCounter++;
      return current(showCorrect);
    }
    return question;
  }

  /**
   * Getter for Players
   *
   * @return map of Clients, Integers
   */
  public Map<Client, Integer> getPlayers() {
    return this.players;
  }

  /**
   * Textual representation of EstimateQuestion
   *
   * @return String description of EstimateQuestion
   */
  @Override
  public String toString() {
    return "Game{"
      + "id='" + id + '\''
      + ", players=" + players
      + ", questionCounter=" + questionCounter
      + ", questions=" + Arrays.toString(questions)
      + '}';
  }

  /**
   * Equals method
   *
   * @param o object we're comparing to
   * @return true if equal, false otherwise
   */

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game game = (Game) o;
    return questionCounter == game.questionCounter && multiplayer == game.multiplayer
      && Objects.equals(id, game.id) && Objects.equals(players, game.players)
      && Arrays.equals(questions, game.questions)
      && Objects.equals(playerListeners, game.playerListeners);
  }

  /**
   * Hash function
   *
   * @return hashcode
   */

  @Override
  public int hashCode() {
    int result = Objects.hash(id, players, questionCounter, multiplayer, playerListeners);
    result = 31 * result + Arrays.hashCode(questions);
    return result;
  }

  /**
   * Stops timing
   */
  public void stopExecTiming() {
    execTiming.shutdownNow();
  }
}
