package commons;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Stores one Activity
 */
@Entity
public class Activity {

  @Id
  public String id;

  public String image_path;
  public String title;
  public long consumption_in_wh;
  public String source;

  @SuppressWarnings("unused")
  private Activity() {

  }

  @SuppressWarnings("unused")
  public Activity(String id, String image_path, String title, long consumption_in_wh, String source) {
    this.id = id;
    this.image_path = image_path;
    this.title = title;
    this.consumption_in_wh = this.consumption_in_wh;
    this.source = source;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Activity)) {
      return false;
    }
    Activity activity = (Activity) o;
    return consumption_in_wh == activity.consumption_in_wh && id.equals(activity.id)
      && image_path.equals(activity.image_path) && title.equals(activity.title) && source.equals(activity.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, image_path, title, consumption_in_wh, source);
  }

  @Override
  public String toString() {
    return "Activity{"
      + "id='" + id + '\''
      + ", image_path='" + image_path + '\''
      + ", title='" + title + '\''
      + ", consumption_in_wh=" + consumption_in_wh
      + ", source='" + source + '\''
      + '}';
  }
}
