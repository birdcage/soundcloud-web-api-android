package lubecki.soundcloud.webapi.android.models;

/**
 * Representation of a SoundCloud Comment.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#comments">Comments
 * Reference</a>
 */
public class Comment {

  public String id;

  public String uri;

  /**
   * Timestamp with format: "yyyy/mm/dd hh:mm:ss +0000" milliseconds??
   */
  public String created_at;

  public String body;

  /**
   * Relevant timestamp in milliseconds.
   */
  public String timestamp;

  public String user_id;

  public MiniUser user;

  public String track_id;
}
