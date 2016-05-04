/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Jacob Lubecki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package lubecki.soundcloud.webapi.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import lubecki.soundcloud.webapi.android.models.AuthenticationResponse;
import lubecki.soundcloud.webapi.android.models.Authenticator;
import retrofit.Callback;
import retrofit.Retrofit;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Class which contains relevant methods for authenticating a user to make full use of the
 * SoundCloud API. A user will have to add an intent filter for their app's callback URI in
 * AndroidManifest.xml for the activity that should handle the Auth Token. Tokens are set to be
 * non-expiring by default but this does not guarantee their longevity. Please see the README file
 * for the soundcloud-web-api-android module for more info.
 */
public class SoundCloudAuthenticator {

  private static AuthService service;

  /**
   * Retrofit interface built solely to authenticate SoundCloud.
   */
  public interface AuthService {

    /**
     * Asynchronously obtains an OAuth Token.
     *
     * @param authMap An {@link Map} defining form-urlencoded auth parameters.
     * @param callback A callback to receive the {@link AuthenticationResponse}.
     */
    @FormUrlEncoded
    @POST("/oauth2/token") void authorize(@FieldMap Map<String, String> authMap,
        Callback<AuthenticationResponse> callback);
  }

  /**
   * Gets the Auth Service so a user can call
   * {@link AuthService#authorize(Map, Callback)}.
   *
   * @return An instance of a {@link AuthService}.
   */
  public static AuthService getAuthService() {
    if (service == null) {
      Retrofit adapter =
          new Retrofit.Builder().baseUrl(SoundCloudAPI.SOUNDCLOUD_API_ENDPOINT).build();

      service = adapter.create(AuthService.class);
    }

    return service;
  }

  /**
   * Describes the method used to give the app permission to make authenticated requests.
   *
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#connect">Connect
   * Reference</a>
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#token">OAuth Reference</a>
   */
  public class GrantType {
    public static final String AUTH_CODE = "authorization_code";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String PASSWORD = "password";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String OAUTH1_TOKEN = "oauth1_token";
  }

  private static final String RESPONSE_TYPE = "code";
  private static final String SCOPE = "non-expiring";
  private static final String DISPLAY = "popup";
  private static final String STATE = "asdf";

  /**
   * Launches an external browser so a user can give the app access. If configured properly,
   * the user will return to the app and then {@link #handleResponse(Intent, String, String, String)}
   * can be used with {@link AuthService#authorize(Map, Callback)}
   * to obtain an Auth Token.
   *
   * @param context Used to start the browser intent.
   * @param redirectUri The users designated redirect URI.
   * @param clientId Secret Client ID.
   * @see <a href="http://soundcloud.com/you/apps">My Apps Page</a>
   */
  public static void openLoginActivity(Context context, String redirectUri, String clientId) {
    String url = "https://www.soundcloud.com/connect?" +
        "client_id=" + clientId +
        "&redirect_uri=" + redirectUri +
        "&response_type=" + RESPONSE_TYPE +
        "&scope=" + SCOPE +
        "&display=" + DISPLAY +
        "&state=" + STATE;

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    context.startActivity(intent);
  }

  /**
   * The intent is filtered by the app's designated Authentication Activity.
   * The {@link Authenticator} provided by this method should be passed to
   * {@link AuthService#authorize(Map, Callback)}.
   * The callback will give an authentication response that will contain an Auth Token.
   *
   * @param intent Intent that was filtered by the activity that should handle authentication.
   * @param redirectUri The URI for the activity that should filter the intent.
   * @param clientId Secret Client ID.
   * @param clientSecret Client Secret.
   * @return An {@link Authenticator} which should be passed to
   * {@link AuthService#authorize(Map, Callback)}.
   * @see <a href="http://soundcloud.com/you/apps">My Apps Page</a>
   */
  public static HashMap<String, String> handleResponse(Intent intent, String redirectUri, String clientId,
      String clientSecret) {
    String uri = intent.getDataString();
    String code = Uri.parse(uri).getQueryParameter(RESPONSE_TYPE);

    if (code != null) {
      HashMap<String, String> fieldMap = new HashMap<>();

      fieldMap.put("client_id", clientId);
      fieldMap.put("client_secret", clientSecret);
      fieldMap.put("code", code);
      fieldMap.put("grant_type", GrantType.AUTH_CODE);
      fieldMap.put("redirect_uri", redirectUri);

      return fieldMap;
    } else {
      throw new IllegalStateException("No code was returned by the request. \n" +
          "Returned URI: " + uri);
    }
  }
}
