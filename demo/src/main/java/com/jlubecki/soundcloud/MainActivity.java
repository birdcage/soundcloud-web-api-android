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

package com.jlubecki.soundcloud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.browser.BrowserSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.AuthTabServiceConnection;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.ChromeTabsSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;
import com.jlubecki.soundcloud.webapi.android.auth.webview.WebViewSoundCloudAuthenticator;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jlubecki.soundcloud.Constants.AUTH_TOKEN_KEY;
import static com.jlubecki.soundcloud.Constants.CLIENT_ID;
import static com.jlubecki.soundcloud.Constants.CLIENT_SECRET;
import static com.jlubecki.soundcloud.Constants.PREFS_NAME;
import static com.jlubecki.soundcloud.Constants.REDIRECT;

public class MainActivity extends AppCompatActivity {

    // Logging
    private static final String TAG = "MainActivity";

    // Constants
    private static final int REQUEST_CODE = 1337;

    // Variables
    private BrowserSoundCloudAuthenticator browserAuthenticator;
    private ChromeTabsSoundCloudAuthenticator tabsAuthenticator;
    private WebViewSoundCloudAuthenticator webViewAuthenticator;

    // Views
    private Button chromeTabAuthButton;
    private Button webViewAuthButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Prepare views
        Button browserAuthButton = (Button) findViewById(R.id.btn_browser_auth);
        chromeTabAuthButton = (Button) findViewById(R.id.btn_chrome_auth);
        webViewAuthButton = (Button) findViewById(R.id.btn_wv_auth);

        // Prepare auth methods

        AuthTabServiceConnection serviceConnection = new AuthTabServiceConnection(new AuthenticationCallback() {
            @Override
            public void onReadyToAuthenticate() {

                // Customize Chrome Tabs
                CustomTabsIntent.Builder builder = tabsAuthenticator.newTabsIntentBuilder()
                        .setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                        .setSecondaryToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

                tabsAuthenticator.setTabsIntentBuilder(builder);

                if(chromeTabAuthButton != null) {
                    chromeTabAuthButton.setEnabled(true);
                }
            }

            @Override
            public void onAuthenticationEnded() {
                Log.i(TAG, "Auth ended.");
            }
        });

        browserAuthenticator = new BrowserSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);
        tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, serviceConnection);
        webViewAuthenticator = new WebViewSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, REQUEST_CODE);

        boolean browserPrepared = browserAuthenticator.prepareAuthenticationFlow();
        boolean tabsPrepared = tabsAuthenticator.prepareAuthenticationFlow();
        boolean webViewPrepared = webViewAuthenticator.prepareAuthenticationFlow();

        browserAuthButton.setEnabled(browserPrepared);
        chromeTabAuthButton.setEnabled(tabsPrepared);
        webViewAuthButton.setEnabled(webViewPrepared);

        Log.d(TAG, "Browser auth enabled: " + browserPrepared);
        Log.d(TAG, "Tabs auth enabled: " + tabsPrepared);
        Log.d(TAG, "WebView auth enabled: " + webViewPrepared);


        browserAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserAuthenticator.launchAuthenticationFlow();
            }
        });

        chromeTabAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabsAuthenticator.launchAuthenticationFlow();
            }
        });

        webViewAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewAuthenticator.launchAuthenticationFlow();
            }
        });
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getTokenFromIntent(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE) {
                getTokenFromIntent(data);
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Authentication canceled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onDestroy() {
        Log.i(TAG, "OnDestroy");

        if(tabsAuthenticator != null) {
            tabsAuthenticator.unbindService();
        }

        super.onDestroy();
    }


    // region Helper

    void getTokenFromIntent(Intent intent) {

        String intentInfo = intent != null ? intent.getDataString() : "Null";
        Log.i(TAG, "Trying to get token from intent data: " + intentInfo);

        if(intent != null && intent.getDataString() != null && intent.getDataString().contains(REDIRECT)) {

            HashMap<String, String> authMap = SoundCloudAuthenticator.handleResponse(intent, REDIRECT, CLIENT_ID, CLIENT_SECRET);

            SoundCloudAuthenticator.AuthService service = tabsAuthenticator.getAuthService(); // Method is final, varies only with clientId used to construct the authenticator
            service.authorize(authMap).enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    Log.i(TAG, "Response was: " + response.raw().toString());

                    AuthenticationResponse authResponse = response.body();

                    if(authResponse != null) {
                        Log.i(TAG, "Auth success -  " + authResponse.access_token);

                        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        preferences.edit().putString(AUTH_TOKEN_KEY, authResponse.access_token).apply();

                        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Log.e(TAG, "Auth failure - " + t.getMessage());
                }
            });
        } else {
            Log.w(this.getClass().getSimpleName(), "Intent data could not be handled.");
        }
    }
}
