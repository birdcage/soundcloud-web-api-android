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

import android.content.Intent;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationStrategy;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.browser.BrowserSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.chrometabs.ChromeTabsSoundCloudAuthenticator;
import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;
import com.jlubecki.soundcloud.webapi.android.auth.webview.WebViewSoundCloudAuthenticator;

import static com.jlubecki.soundcloud.Constants.AUTH_TOKEN_KEY;
import static com.jlubecki.soundcloud.Constants.CLIENT_ID;
import static com.jlubecki.soundcloud.Constants.CLIENT_SECRET;
import static com.jlubecki.soundcloud.Constants.PREFS_NAME;
import static com.jlubecki.soundcloud.Constants.REDIRECT;

public class MainActivity extends AppCompatActivity {

    // Logging
    private static final String TAG = "MainActivity";

    // Constants
    private static final int REQUEST_CODE_AUTHENTICATE = 1337;

    // Variables
    private SoundCloudAuthenticator mAuthenticator;

    private AuthenticationStrategy strategy;
    private AuthenticationCallback callback = new AuthenticationCallback() {
        @Override
        public void onReadyToAuthenticate(SoundCloudAuthenticator authenticator) {

            // Customize Chrome Tabs
            if(authenticator instanceof ChromeTabsSoundCloudAuthenticator) {
                int toolbarColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                int secondaryToolbarColor = ContextCompat.getColor(MainActivity.this, R.color.colorAccent);

                ChromeTabsSoundCloudAuthenticator tabsAuthenticator = (ChromeTabsSoundCloudAuthenticator) authenticator;
                CustomTabsIntent.Builder builder = tabsAuthenticator.newTabsIntentBuilder()
                        .setToolbarColor(toolbarColor)
                        .setSecondaryToolbarColor(secondaryToolbarColor);

                tabsAuthenticator.setTabsIntentBuilder(builder);
            }

            mAuthenticator = authenticator;
            launchAuthButton.setEnabled(true);
        }
    };

    // Views
    private Button launchAuthButton;
    private Button openPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Prepare views
        launchAuthButton = (Button) findViewById(R.id.btn_begin_auth);
        openPlayerButton = (Button) findViewById(R.id.btn_skip_auth);

        launchAuthButton.setEnabled(false);

        // Prepare auth methods

        ChromeTabsSoundCloudAuthenticator tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);
        BrowserSoundCloudAuthenticator browserAuthenticator = new BrowserSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);
        WebViewSoundCloudAuthenticator webViewAuthenticator = new WebViewSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, REQUEST_CODE_AUTHENTICATE);

        strategy = new AuthenticationStrategy.Builder(this)
                .addAuthenticator(tabsAuthenticator) // Tries this first
                .addAuthenticator(browserAuthenticator) // Then tries this
                .addAuthenticator(webViewAuthenticator) // Finally tries this
                .setCheckNetwork(true) // Makes sure the internet is connected first.
                .build();

        strategy.beginAuthentication(callback);

        setupClickListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTokenFromIntent(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_AUTHENTICATE:
                handleAuthRequestCode(requestCode, resultCode, data);
                break;

            default:
                Log.i(TAG, "Other activity result: " + requestCode);
                break;
        }
    }

    @Override
    public void onDestroy() {
        if(mAuthenticator != null) {
            mAuthenticator.release(); // Need to call this if using Chrome Tabs.
        }

        super.onDestroy();
    }

    // region Helper

    void setupClickListeners() {
        launchAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthenticator.launchAuthenticationFlow();
            }
        });

        openPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayer();
            }
        });
    }

    void getTokenFromIntent(Intent intent) {
        String intentInfo = intent != null ? intent.getDataString() : "Null";
        Log.i(TAG, "Trying to get token from intent data: " + intentInfo);

        if (strategy != null && strategy.canAuthenticate(intent)) {
            strategy.getToken(intent, CLIENT_SECRET, new AuthenticationStrategy.ResponseCallback() {
                @Override
                public void onAuthenticationResponse(AuthenticationResponse response) {
                    switch (response.getType()) {
                        case TOKEN:
                            saveToken(response.access_token);
                            openPlayer();
                            break;

                        default:
                            Log.e(TAG, response.toString());
                            break;
                    }
                }

                @Override
                public void onAuthenticationFailed(Throwable throwable) {
                    Log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

    private void handleAuthRequestCode(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_AUTHENTICATE) {
            String error = "Code: " + requestCode + " should be: " + REQUEST_CODE_AUTHENTICATE;
            Log.e(TAG, error);

            return;
        }

        if (resultCode == RESULT_OK) {
            getTokenFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
            Log.w(TAG, "Authentication was canceled.");
        } else {
            Log.w(TAG, "Unhandled result code: " + resultCode);
        }
    }

    private void saveToken(String token) {
        Log.i(TAG, "Token saved -  " + token);

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putString(AUTH_TOKEN_KEY, token)
                .apply();
    }

    private void openPlayer() {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // endregion
}
