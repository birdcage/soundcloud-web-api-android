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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jlubecki.soundcloud.webapi.android.R;

/**
 * Created by Jacob on 8/26/16.
 */
public class AuthenticationActivity extends AppCompatActivity {

    public static final String EXTRA_AUTH_URL = "com.jlubecki.soundcloud.webapi.android.auth.webview.AuthenticationActivity.EXTRA_AUTH_URL";
    public static final String EXTRA_AUTH_REDIRECT = "com.jlubecki.soundcloud.webapi.android.auth.webview.AuthenticationActivity.EXTRA_AUTH_REDIRECT";

    private String redirectUri;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_auth);

        WebView webView = (WebView) findViewById(R.id.wv_auth);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewAuthClient());

        String loginUrl = getIntent().getStringExtra(EXTRA_AUTH_URL);
        redirectUri = getIntent().getStringExtra(EXTRA_AUTH_REDIRECT);

        if (loginUrl != null && redirectUri != null) {
            webView.loadUrl(loginUrl);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private class WebViewAuthClient extends WebViewClient {
        Intent resultIntent = new Intent();

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (url.startsWith(redirectUri)) {
                Uri data = Uri.parse(url);
                resultIntent.setData(data);

                if (url.contains("?code=")) {
                    setResult(RESULT_OK, resultIntent);
                } else {
                    setResult(RESULT_CANCELED, resultIntent);
                }

                finish();
            }
        }
    }
}
