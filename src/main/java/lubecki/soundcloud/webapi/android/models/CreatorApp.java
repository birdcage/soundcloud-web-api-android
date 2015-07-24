package lubecki.soundcloud.webapi.android.models;

/**
 * Representation of the app that was used to publish a sound on SoundCloud.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#created_with">"Created With"
 * Reference</a> and
 * <a href="https://developers.soundcloud.com/docs/api/reference#apps">Apps Reference</a>
 */
public class CreatorApp {

  /**
   * ID of app used to create a sound.
   */
  public String id;

  /**
   * API resource url.
   */
  public String uri;

  /**
   * SoundCloud url to user.
   */
  public String permalink_url;

  /**
   * External url to app page.
   */
  public String external_url;

  /**
   * Creator's user name.
   */
  public String creator;
}
