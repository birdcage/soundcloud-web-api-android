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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.browser.BrowserSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.AuthTabServiceConnection;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.ChromeTabsSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;
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

    private BrowserSoundCloudAuthenticator browserAuthenticator;
    private ChromeTabsSoundCloudAuthenticator tabsAuthenticator;
    private boolean tabsDidConnect = false;

    private Button chromeTabAuthButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Log.i(TAG, "OnCreate");

        // Prepare views
        Button browserAuthButton = (Button) findViewById(R.id.btn_browser_auth);
        chromeTabAuthButton = (Button) findViewById(R.id.btn_chrome_auth);
        chromeTabAuthButton.setEnabled(false);

        // Prepare auth methods
        browserAuthenticator = new BrowserSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);

        browserAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserAuthenticator.launchAuthenticationFlow();
            }
        });

        AuthTabServiceConnection serviceConnection = new AuthTabServiceConnection(new AuthenticationCallback() {
            @Override
            public void onReadyToAuthenticate() {
                if(chromeTabAuthButton != null) {
                    chromeTabAuthButton.setEnabled(true);
                }
            }

            @Override
            public void onAuthenticationEnded() {
                Log.i(TAG, "Auth ended.");
            }
        });

        tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, serviceConnection);

        // Customize Chrome Tabs
        CustomTabsIntent.Builder builder = tabsAuthenticator.newTabsIntentBuilder()
            .setToolbarColor(getColorCompat(R.color.colorPrimary))
            .setSecondaryToolbarColor(getColorCompat(R.color.colorAccent));

        tabsAuthenticator.setTabsIntentBuilder(builder);

        chromeTabAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabsAuthenticator.launchAuthenticationFlow();
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();

        if(tabsAuthenticator != null) {
            tabsDidConnect = tabsAuthenticator.prepareAuthenticationFlow();

            Log.i(TAG, "Tab auth did connect: " + tabsDidConnect);
        }
    }

    @Override protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String intentInfo = intent != null ? intent.getDataString() : "Null intent.";

        Log.i(TAG, "Lifecycle method onResume with intent info: " + intentInfo);

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
            Log.w(TAG, "Other new intent not handled.");
        }


    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    @Override public void onDestroy() {
        Log.i(TAG, "OnDestroy");

        if(tabsAuthenticator != null && tabsDidConnect) {
            tabsAuthenticator.unbindService();
        }

        super.onDestroy();
    }

    @SuppressLint("deprecation")
    private int getColorCompat(@ColorRes int color) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return getColor(color);
        } else {
            return getResources().getColor(color);
        }
    }
}
