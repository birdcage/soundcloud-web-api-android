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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import lubecki.soundcloud.webapi.android.models.Track;
import lubecki.soundcloud.webapi.android.query.Pager;
import lubecki.soundcloud.webapi.android.query.TrackQuery;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

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

    RestAdapter adapter = new RestAdapter.Builder().setClient(new OkClient())
        .setEndpoint(SOUNDCLOUD_API_ENDPOINT)
        .setRequestInterceptor(new SoundCloudRequestInterceptor())
        .setConverter(new GsonConverter(gson))
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

  private class SoundCloudRequestInterceptor implements RequestInterceptor {
    @Override public void intercept(RequestFacade request) {
      request.addQueryParam("client_id", clientId);
      if (token != null) {
        request.addQueryParam("oauth_token", token);
      }
    }
  }
}
