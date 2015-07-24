package lubecki.soundcloud.webapi.android.models;

/**
 * A representation of a SoundCloud connection. A connection is an external profile that is
 * connected to a SoundCloud account.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#connections">
 *   SoundCloud Connection Reference</a>
 */
public class Connection {

  public String created_at;

  public String display_name;

  public String id;

  public String post_favorite;

  public String post_publish;

  public String service;

  public String type;

  public String uri;
}
