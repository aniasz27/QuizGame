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
  /**
   * Empty Activity Constructor
   */
  private Activity() {
  }

  @SuppressWarnings("unused")
  /**
   * Activity Constructor
   *
   * @param id Activity id
   * @param author author of the activity
   * @param image_path path to the image
   * @param title title of the activity
   * @param consumption_in_wh consumption
   * @param source source
   */
  public Activity(String id, String author, String image_path, String title, long consumption_in_wh, String source) {
    this.id = id;
    this.author = author;
    this.image_path = image_path;
    this.title = title;
    this.consumption_in_wh = consumption_in_wh;
    this.source = source;
  }

  /**
   * Return Activity id
   *
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Getter for author
   *
   * @return String author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Getter for image path
   *
   * @return String image_path
   */
  public String getImage_path() {
    return image_path;
  }

  /**
   * Getter for title
   *
   * @return String title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Getter for consumption
   *
   * @return long consumption
   */
  public long getConsumption_in_wh() {
    return consumption_in_wh;
  }

  /**
   * Getter for source
   *
   * @return String source
   */
  public String getSource() {
    return source;
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
    if (!(o instanceof Activity)) {
      return false;
    }
    Activity activity = (Activity) o;
    return consumption_in_wh == activity.consumption_in_wh && id.equals(activity.id) && author.equals(activity.author)
      && image_path.equals(activity.image_path) && title.equals(activity.title) && source.equals(activity.source);
  }

  /**
   * Hash function for Activity
   *
   * @return hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, author, image_path, title, consumption_in_wh, source);
  }

  /**
   * Textual representation of Activity
   *
   * @return String description of activity
   */
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
