package lubecki.soundcloud.webapi.android;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.Date;

import lubecki.soundcloud.webapi.android.models.Track;
import lubecki.soundcloud.webapi.android.query.Pager;
import lubecki.soundcloud.webapi.android.query.TrackQuery;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Class which builds a {@link SoundCloudService} to access the SoundCloud API. To make
 * authenticated requests, use the {@link SoundCloudAuthenticator} class to obtain an access token
 * and then call {@link #setToken(String)}.
 */
public class SoundCloudAPI {

  public static final String SOUNDCLOUD_API_ENDPOINT = "https://api.soundcloud.com";

  private final SoundCloudService service;

  private final String clientId;
  private String token;

  /**
   * Creates a {@link SoundCloudService}. Serializes with JSON.
   *
   * @param clientId Client ID provided by SoundCloud.
   */
  public SoundCloudAPI(String clientId) {
    this.clientId = clientId;

    Gson gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    OkHttpClient client = new OkHttpClient();
    client.interceptors().add(new SoundCloudInterceptor());

    Retrofit adapter = new Retrofit.Builder().client(client)
        .baseUrl(SOUNDCLOUD_API_ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    service = adapter.create(SoundCloudService.class);
  }

  /**
   * Gives access to a {@link SoundCloudService}.
   *
   * @return The {@link SoundCloudService} created by this {@link SoundCloudAPI}.
   */
  public SoundCloudService getService() {
    return service;
  }

  /**
   * Sets the auth token needed by the service in order to make authenticated requests.
   *
   * @param token The OAuth token to use for authenticated requests.
   */
  public void setToken(String token) {
    this.token = token;
  }

  private class SoundCloudInterceptor implements Interceptor {
    @Override public Response intercept(Chain chain) throws IOException {

      Request request = chain.request();

      HttpUrl.Builder builder = request.httpUrl().newBuilder();

      builder.addEncodedQueryParameter("client_id", clientId);
      if (token != null) {
        builder.addEncodedQueryParameter("oauth_token", token);
      }

      return chain.proceed(request);
    }
  }
}
