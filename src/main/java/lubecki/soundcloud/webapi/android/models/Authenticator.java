package lubecki.soundcloud.webapi.android.models;

/**
 * Representation of the auth criteria needed to obtain a SoundCloud OAuth token.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#token">Auth Reference</a>
 */
public class Authenticator {

  public String client_id;

  public String client_secret;

  public String redirect_uri;

  public String grant_type;

  public String code;
}
