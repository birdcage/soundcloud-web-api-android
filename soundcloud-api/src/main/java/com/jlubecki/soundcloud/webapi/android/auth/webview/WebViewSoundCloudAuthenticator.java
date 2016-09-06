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

package com.jlubecki.soundcloud.webapi.android.auth.webview;

import android.app.Activity;
import android.content.Intent;

import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;

/**
 * Created by Jacob on 8/26/16.
 */
public class WebViewSoundCloudAuthenticator extends SoundCloudAuthenticator {

    private final Activity context;
    private final int requestCode;

    /**
     * Creates a new SoundCloudAuthenticator.
     *
     * @param clientId    Client ID of the application requesting authorization.
     * @param redirectUri Redirect URI of the application requesting authorization
     */
    public WebViewSoundCloudAuthenticator(String clientId, String redirectUri, Activity context, int requestCode) {
        super(clientId, redirectUri);

        this.context = context;
        this.requestCode = requestCode;
    }

    @Override
    public boolean prepareAuthenticationFlow(AuthenticationCallback callback) {
        callback.onReadyToAuthenticate(this);

        // WebView should always be available...
        return true;
    }

    @Override
    public void launchAuthenticationFlow() {
        Intent loginIntent = new Intent(context, AuthenticationActivity.class);
        loginIntent.putExtra(AuthenticationActivity.EXTRA_AUTH_URL, loginUrl());
        loginIntent.putExtra(AuthenticationActivity.EXTRA_AUTH_REDIRECT, redirectUri);
        context.startActivityForResult(loginIntent, requestCode);
    }
}
