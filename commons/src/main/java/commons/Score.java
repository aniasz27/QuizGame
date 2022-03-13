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
  public int points;

  @SuppressWarnings("unused")
  private Score() {

  }

  @SuppressWarnings("unused")
  public Score(String player, int points) {
    this.player = player;
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
    Score score1 = (Score) o;
    return id == score1.id && points == score1.points && Objects.equals(player, score1.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, player, points);
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

  public void setPlayer(String player) {
    this.player = player;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void addPoints(int points) {
    this.points += points;
  }
}
