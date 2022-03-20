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
  private Score() {

  }

  @SuppressWarnings("unused")
  public Score(String player, String name, int points) {
    this.player = player;
    this.name = name;
    this.points = points;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(id, player, name, points);
  }

  @Override
  public String toString() {
    return "Score{"
      + "id='" + id + '\''
      + ", player='" + player + '\''
      + ", points='" + points + '\''
      + '}';
  }

  public String getPlayer() {
    return player;
  }

  public int getPoints() {
    return points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public String getName() {
    return name;
  }
}
