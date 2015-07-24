package lubecki.soundcloud.webapi.android.models;

/**
 * Miniature representation of a {@link User}. Makes it easier to identify accessible fields when
 * getting user details from a {@link Track}, {@link Comment}, etc.
 */
public class MiniUser {

  public String avatar_url;

  /**
   * Integer value of a user's ID.
   */
  public String id;

  public String kind;

  public String last_modified;

  public String permalink;

  public String permalink_url;

  public String uri;

  public String username;
}
