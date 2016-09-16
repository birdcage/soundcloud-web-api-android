/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jacob Lubecki
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

package com.jlubecki.soundcloud.webapi.android.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.jlubecki.soundcloud.webapi.android.SoundCloudAPI;
import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Jacob on 6/23/16.
 */
public abstract class SoundCloudAuthenticator {

    private AuthService service;

    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "non-expiring";
    private static final String DISPLAY = "popup";
    private static final String STATE = "asdf";

    protected final String clientId;
    protected final String redirectUri;

    /**
     * Creates a new SoundCloudAuthenticator.
     *
     * @param clientId    Client ID of the application requesting authorization.
     * @param redirectUri Redirect URI of the application requesting authorization
     */
    public SoundCloudAuthenticator(String clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    /**
     * Method to create asynchronous authentication preparation flows.
     *
     * @return true if the method was able to execute all necessary preparation steps.
     */
    protected abstract boolean prepareAuthenticationFlow(AuthenticationCallback callback);

    /**
     * Method which should handle launching the authentication flow once it is prepared.
     */
    public abstract void launchAuthenticationFlow();

    /**
     * Override this method to deallocate any resources or release an implementation of a
     * SoundCloudAuthenticator. For an example, see {@link com.jlubecki.soundcloud.webapi.android.auth.chrometabs.ChromeTabsSoundCloudAuthenticator}.
     */
    public void release() { }

    protected final String loginUrl() {
        return "https://www.soundcloud.com/connect?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=" + RESPONSE_TYPE +
                "&scope=" + SCOPE +
                "&display=" + DISPLAY +
                "&state=" + STATE;
    }

    protected final void addReferrerToIntent(Intent intent, String packageName) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String referrer = Intent.URI_ANDROID_APP_SCHEME + "//" + packageName;
            intent.putExtra(Intent.EXTRA_REFERRER_NAME, referrer);
        }
    }

    public boolean canAuthenticate(Intent intent) {
        return intent != null &&
                intent.getDataString() != null &&
                intent.getDataString().contains(redirectUri);
    }

    /**
     * The intent is filtered by the app's designated Authentication Activity.
     * The callback will give an authentication code that can be used to obtain an Auth Token.
     *
     * @param intent       Intent that was filtered by the activity that should handle authentication.
     * @param clientSecret Client Secret.
     * @return a HashMap which should be passed to {@link AuthService#authorize(Map)}. Returns null if
     * no code was found in the intent data.
     * @see <a href="http://soundcloud.com/you/apps">My Apps Page</a>
     */
    public HashMap<String, String> handleResponse(Intent intent, String clientSecret) {
        if (canAuthenticate(intent)) {
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
            }
        }

        return null;
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

    /**
     * Retrofit interface built solely to authenticate SoundCloud.
     */
    public interface AuthService {

        /**
         * Asynchronously obtains an OAuth Token.
         *
         * @param authMap An {@link Map} defining form-urlencoded auth parameters.
         * @return the call that can be run to access the API resource.
         */
        @FormUrlEncoded
        @POST("oauth2/token")
        Call<AuthenticationResponse> authorize(@FieldMap Map<String, String> authMap);
    }

    /**
     * Gets the Auth Service so a user can call
     * {@link AuthService#authorize(Map)}.
     *
     * @return An instance of a {@link AuthService}.
     */
    public final AuthService getAuthService() {
        if (service == null) {
            OkHttpClient client =
                    new OkHttpClient.Builder().addInterceptor(new AuthInterceptor()).build();

            Retrofit adapter = new Retrofit.Builder()
                    .baseUrl(SoundCloudAPI.SOUNDCLOUD_API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = adapter.create(AuthService.class);
        }

        return service;
    }

    protected class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            HttpUrl url = request.url().newBuilder()
                    .addEncodedQueryParameter("client_id", clientId)
                    .build();

            Request newRequest = request.newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(newRequest);
        }
    }
}
