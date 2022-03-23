package commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Game {
  public String id;
  public Map<Client, Integer> players = new HashMap<>();
  public int questionCounter = 0;
  public boolean showedLeaderboard = false;
  public Question[] questions;

  @SuppressWarnings("unused")
  public Game() {
  }

  /**
   * Creates a new game without players
   *
   * @param id the game's ID
   */
  public Game(String id) {
    this.id = id;
  }

  /**
   * Creates a new game with players from a collection with scores set to 0
   *
   * @param id        the game's ID
   * @param players   the players in the game
   * @param questions the questions in this game
   */
  public Game(String id, Collection<Client> players, Collection<Question> questions) {
    this.id = id;
    this.players = players.stream().collect(Collectors.toMap(client -> client, client -> 0));
    this.questions = questions.toArray(new Question[20]);
  }

  /**
   * Creates a new game with players from a collection with scores set to 0
   *
   * @param id        the game's ID
   * @param players   the players in the game
   * @param questions the questions in this game
   */
  public Game(String id, Collection<Client> players, Question[] questions) {
    this.id = id;
    this.players = players.stream().collect(Collectors.toMap(client -> client, client -> 0));
    this.questions = questions;
  }

  /**
   * Creates a new game with players from a map
   *
   * @param id        the game's ID
   * @param players   map of players and their scores
   * @param questions the questions in this game
   */
  public Game(String id, Map<Client, Integer> players, Collection<Question> questions) {
    this.id = id;
    this.players = players;
    this.questions = questions.toArray(new Question[20]);
  }

  /**
   * Creates a new game with players from a map
   *
   * @param id        the game's ID
   * @param players   map of players and their scores
   * @param questions the questions in this game
   */
  public Game(String id, Map<Client, Integer> players, Question[] questions) {
    this.id = id;
    this.players = players;
    this.questions = questions;
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
  public Question next() {
    Question question;
    if (questionCounter >= 20) {
      question = new EndScreen();
      question.number = questionCounter;
      return question;
    }
    if (questionCounter == 10 && !showedLeaderboard) {
      showedLeaderboard = true;
      question = new EndScreen();
      question.number = questionCounter;
      return question;
    }
    return questions[questionCounter++];
  }

  /**
   * Gets the next question.
   *
   * @return the next question
   */
  public Question current() {
    Question question;
    if (questionCounter >= 20) {
      question = new EndScreen();
      question.number = questionCounter;
      return question;
    }
    if (questionCounter == 10 && !showedLeaderboard) {
      question = new EndScreen();
      question.number = questionCounter;
      return question;
    }
    return questions[questionCounter];
  }

  @Override
  public String toString() {
    return "Game{"
      + "id='" + id + '\''
      + ", players=" + players
      + ", questionCounter=" + questionCounter
      + ", showedLeaderboard=" + showedLeaderboard
      + ", questions=" + Arrays.toString(questions)
      + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return Objects.equals(id, ((Game) o).id) && Objects.equals(players, ((Game) o).players)
      && questionCounter == ((Game) o).questionCounter && showedLeaderboard == ((Game) o).showedLeaderboard
      && Objects.equals(questions, ((Game) o).questions);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, players, questionCounter, showedLeaderboard);
    result = 31 * result + Arrays.hashCode(questions);
    return result;
  }
}
