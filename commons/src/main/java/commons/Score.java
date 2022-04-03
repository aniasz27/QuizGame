package commons;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long id;

  public String player;
  public String name;
  public int points;

  @SuppressWarnings("unused")
  /**
   * Empty Constructor
   */
  private Score() {

  }

  @SuppressWarnings("unused")
  /**
   * Score Constructor
   *
   * @param player player id
   * @param name player username
   * @param points player points
   */
  public Score(String player, String name, int points) {
    this.player = player;
    this.name = name;
    this.points = points;
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
    Score score = (Score) o;
    return points == score.points && Objects.equals(player, score.player)
      && Objects.equals(name, score.name);
  }

  /**
   * Hash function
   *
   * @return hashcode
   */

  @Override
  public int hashCode() {
    return Objects.hash(id, player, name, points);
  }

  /**
   * Textual representation of Score
   *
   * @return String description of Score
   */

  @Override
  public String toString() {
    return "Score{"
      + "id='" + id + '\''
      + ", player='" + player + '\''
      + ", points='" + points + '\''
      + '}';
  }

  /**
   * Getter for the player
   *
   * @return String representing the playerId
   */
  public String getPlayer() {
    return player;
  }

  /**
   * Getter for points field
   *
   * @return int representing the number of points the player has
   */
  public int getPoints() {
    return points;
  }

  /**
   * Increases points
   *
   * @param points amount of points that should be added
   */
  public void addPoints(int points) {
    this.points += points;
  }

  /**
   * Getter for the name attribute
   *
   * @return String with the username of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for points field
   *
   * @param amount of points
   */
  public void setPoints(int amount) {
    this.points = amount;
  }
}
