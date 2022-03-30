package commons;

import java.util.Comparator;

public class ScoreComparator implements Comparator {

  @Override
  public int compare(Object o1, Object o2) {
    Score a1 = (Score) o1;
    Score a2 = (Score) o2;
    if (a1.getPoints() == a2.getPoints()) {
      return 0;
    } else if (a1.getPoints() > a2.getPoints()) {
      return -1;
    } else {
      return 1;
    }
  }
}
