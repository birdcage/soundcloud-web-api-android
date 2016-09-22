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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jlubecki.soundcloud.webapi.android.auth.models.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jacob on 9/1/16.
 */
public class AuthenticationStrategy {

    private static final String TAG = "AuthenticationStrategy";

    private final List<SoundCloudAuthenticator> authenticators;
    private final Context context;
    private OnNetworkFailureListener onNetworkFailureListener;
    private boolean shouldCheckNetwork = false;
    private SoundCloudAuthenticator authenticator;

    private AuthenticationStrategy(@NonNull Context context, @NonNull List<SoundCloudAuthenticator> authenticators) {
        this.context = context;
        this.authenticators = authenticators;
    }

    public boolean beginAuthentication(AuthenticationCallback callback) {
        if (shouldCheckNetwork && !networkIsConnected()) return false; // Not connected to the internet.

        for(SoundCloudAuthenticator authenticator : authenticators) {
            if(authenticator.prepareAuthenticationFlow(callback)) {
                this.authenticator = authenticator;
                return true; // Prepared successfully. Will execute callback.
            }
        }

        return false;
    }

    public boolean canAuthenticate(Intent intent) {
        return authenticator != null &&
                authenticator.canAuthenticate(intent);
    }

    public void getToken(Intent intent, String clientSecret, final ResponseCallback callback) {
        Map<String, String> authMap = authenticator.handleResponse(intent, clientSecret);

        if (authMap != null) {
            authenticator.getAuthService()
                    .authorize(authMap)
                    .enqueue(new Callback<AuthenticationResponse>() {
                        @Override
                        public void onResponse(Call<AuthenticationResponse> call,
                                               Response<AuthenticationResponse> response) {
                            AuthenticationResponse authResponse = response.body();

                            if (authResponse != null) {
                                callback.onAuthenticationResponse(authResponse);
                            } else {
                                String errorReason = "The authentication response was null.";
                                AssertionError error = new AssertionError(errorReason);
                                callback.onAuthenticationFailed(error);
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                            callback.onAuthenticationFailed(t);
                        }
                    });
        } else {
            String errorReason = "Authentication parameter map was null.";
            AssertionError error = new AssertionError(errorReason);
            callback.onAuthenticationFailed(error);
        }
    }

    private boolean networkIsConnected() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = manager.getActiveNetworkInfo().isConnected();

        if (!isConnected) {
            String errorReason = "Authentication Failed: Network was not connected.";

            if (onNetworkFailureListener != null) {
                AssertionError error = new AssertionError(errorReason);
                onNetworkFailureListener.onFailure(error);
            } else {
                Log.e(TAG, errorReason);
            }
        }

        return isConnected;
    }

    public static class Builder {

        private final List<SoundCloudAuthenticator> authenticators;
        private final Context context;
        private OnNetworkFailureListener onNetworkFailureListener;
        private boolean shouldCheckNetwork = false;

        public Builder(@NonNull Context context) {
            this.context = context;
            this.authenticators = new ArrayList<>();
        }

        public Builder addAuthenticator(SoundCloudAuthenticator authenticator) {
            authenticators.add(authenticator);

            return this;
        }

        public Builder addAuthenticators(List<SoundCloudAuthenticator> authenticators) {
            this.authenticators.addAll(authenticators);

            return this;
        }

        public Builder setCheckNetwork(boolean shouldDoCheck) {
            this.shouldCheckNetwork = shouldDoCheck;

            return this;
        }

        public Builder onFailure(OnNetworkFailureListener listener) {
            this.onNetworkFailureListener = listener;

            return this;
        }

        public AuthenticationStrategy build() {
            AuthenticationStrategy strategy = new AuthenticationStrategy(context, authenticators);
            strategy.shouldCheckNetwork = shouldCheckNetwork;
            strategy.onNetworkFailureListener = onNetworkFailureListener;

            return strategy;
        }
    }

    public interface OnNetworkFailureListener {
        void onFailure(Throwable throwable);
    }

    public interface ResponseCallback {
        void onAuthenticationResponse(AuthenticationResponse response);

        void onAuthenticationFailed(Throwable throwable);
    }
}
