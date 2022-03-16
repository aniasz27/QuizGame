package commons;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Stores one Activity
 */
@Entity
public class Activity {

  @Id
  public String id;

  public String author;
  public String image_path;
  public String title;
  public long consumption_in_wh;
  @Lob
  @Column
  public String source;

  @SuppressWarnings("unused")
  private Activity() {
  }

  @SuppressWarnings("unused")
  public Activity(String id, String author, String image_path, String title, long consumption_in_wh, String source) {
    this.id = id;
    this.author = author;
    this.image_path = image_path;
    this.title = title;
    this.consumption_in_wh = consumption_in_wh;
    this.source = source;
  }

  public String getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getImage_path() {
    return image_path;
  }

  public String getTitle() {
    return title;
  }

  public long getConsumption_in_wh() {
    return consumption_in_wh;
  }

  public String getSource() {
    return source;
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
    return consumption_in_wh == activity.consumption_in_wh && id.equals(activity.id) && author.equals(activity.author)
      && image_path.equals(activity.image_path) && title.equals(activity.title) && source.equals(activity.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, author, image_path, title, consumption_in_wh, source);
  }

  @Override
  public String toString() {
    return "Activity{"
      + "id='" + id + '\''
      + ", author='" + author + '\''
      + ", image_path='" + image_path + '\''
      + ", title='" + title + '\''
      + ", consumption_in_wh=" + consumption_in_wh
      + ", source='" + source + '\''
      + '}';
  }
}
