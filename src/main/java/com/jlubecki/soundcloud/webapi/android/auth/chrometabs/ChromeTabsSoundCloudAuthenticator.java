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

package com.jlubecki.soundcloud.webapi.android.auth.chrometabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;

/**
 * Class which contains relevant methods for authenticating a user to make full use of the
 * SoundCloud API. A user will have to add an intent filter for their app's callback URI in
 * AndroidManifest.xml for the activity that should handle the Auth Token. Tokens are set to be
 * non-expiring by default but this does not guarantee their longevity. Please see the README file
 * for the soundcloud-web-api-android module for more info.
 */
public class ChromeTabsSoundCloudAuthenticator extends SoundCloudAuthenticator {

  private final Activity context;
  private final String browserPackageName;
  private final AuthTabServiceConnection serviceConnection;

  public ChromeTabsSoundCloudAuthenticator(String clientId, String redirectUri, Activity context, AuthTabServiceConnection serviceConnection) {
    super(clientId, redirectUri);

    this.context = context;
    this.serviceConnection = serviceConnection;
    this.browserPackageName = CustomTabsClient.getPackageName(context, null);
  }

  public ChromeTabsSoundCloudAuthenticator(String clientId, String redirectUri, Activity context) {
    super(clientId, redirectUri);

    this.context = context;
    this.serviceConnection = new AuthTabServiceConnection(new AuthenticationCallback() {
      @Override public void onReadyToAuthenticate() {
        launchAuthenticationFlow();
      }

      @Override public void onAuthenticationEnded() {
        // Do nothing.
      }
    });

    this.browserPackageName = CustomTabsClient.getPackageName(context, null);
  }

  @Override public boolean prepareAuthenticationFlow() {
    return CustomTabsClient.bindCustomTabsService(context, browserPackageName, serviceConnection);
  }

  @Override public void launchAuthenticationFlow() {
    CustomTabsSession tabsSession = serviceConnection.getSession();
    CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder(tabsSession).build();

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
      String referrer = Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName();
      tabsIntent.intent.putExtra(Intent.EXTRA_REFERRER_NAME, referrer);
    }

    tabsIntent.intent.setPackage(browserPackageName);
    tabsIntent.launchUrl(context, Uri.parse(loginUrl()));
  }

  public void unbindService() {
    context.unbindService(serviceConnection);
  }
}
