package lubecki.soundcloud.webapi.android.models;

/**
 * Representation of the API response from the authentication endpoint.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#token">Auth Reference</a>
 */
public class AuthenticationResponse {

  // ~~ CONSTANTS ~~

  public static final String TOKEN = "access_token";
  public static final String ERROR = "error";
  public static final String UNKNOWN = "unknown";

  // ~~ FIELDS ~~

  public String access_token;

  public String scope;

  public String error;

  /**
   * Helper method to determine authentication response type.
   *
   * @return The type of the authentication response.
   */
  public String getType() {
    if(access_token != null) {
      return TOKEN;
    }

    if(error != null) {
      return ERROR;
    }

    return UNKNOWN;
  }
}
