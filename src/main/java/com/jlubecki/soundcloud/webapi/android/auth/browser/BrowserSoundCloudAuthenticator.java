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

package com.jlubecki.soundcloud.webapi.android.auth.browser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;
import java.util.Map;

/**
 * Created by Jacob on 6/23/16.
 */
public class BrowserSoundCloudAuthenticator extends SoundCloudAuthenticator {

  private final Activity context;

  public BrowserSoundCloudAuthenticator(String clientId, String redirectUri, Activity context) {
    super(clientId, redirectUri);

    this.context = context;
  }

  @Override public boolean prepareAuthenticationFlow() {
    // Launches synchronously in this implementation, no preparation needed.
    return true;
  }

  /**
   * Launches an external browser so a user can give the app access. If configured properly,
   * the user will return to the app and then {@link #handleResponse(Intent, String, String, String)}
   * can be used with {@link AuthService#authorize(Map)}
   * to obtain an Auth Token.
   *.
   * @see <a href="http://soundcloud.com/you/apps">My Apps Page</a>
   */
  @Override public void launchAuthenticationFlow() {
    Uri authUri = Uri.parse(loginUrl());

    Intent loginIntent = new Intent(Intent.ACTION_VIEW);
    loginIntent.setPackage(getBrowserPackageName());
    loginIntent.setData(authUri);
    context.startActivity(loginIntent);
  }

  private String getBrowserPackageName() {
    String packageName = CustomTabsClient.getPackageName(context, null);

    if(packageName == null) {
      Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));

      PackageManager manager = context.getPackageManager();
      ResolveInfo info = manager.resolveActivity(webIntent, 0);

      if(info != null) {
        packageName = info.activityInfo.packageName;
      } else {
        packageName = "com.android.browser";
      }
    }

    return packageName;
  }
}
