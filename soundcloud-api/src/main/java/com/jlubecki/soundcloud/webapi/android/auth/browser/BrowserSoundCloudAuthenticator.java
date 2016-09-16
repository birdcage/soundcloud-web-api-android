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

import com.jlubecki.soundcloud.webapi.android.auth.AuthenticationCallback;
import com.jlubecki.soundcloud.webapi.android.auth.SoundCloudAuthenticator;

import java.util.List;
import java.util.Map;

/**
 * Created by Jacob on 6/23/16.
 * <p/>
 * Used to beginAuthentication SoundCloud via an installed Browser on the user's device.
 */
public class BrowserSoundCloudAuthenticator extends SoundCloudAuthenticator {

    private final Activity context;
    private final String browserPackageName;
    private final Intent launchIntent;

    /**
     * Creates a {@link SoundCloudAuthenticator} that will launch the authentication in a browser.
     *
     * @param clientId    Client ID of the application requesting authorization.
     * @param redirectUri Redirect URI of the application requesting authorization
     * @param context     Activity from which authentication is being launched.
     */
    public BrowserSoundCloudAuthenticator(String clientId, String redirectUri, Activity context) {
        super(clientId, redirectUri);

        this.context = context;
        this.browserPackageName = getBrowserPackageName();
        this.launchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loginUrl()));
    }

    /**
     * Browser implementation has no warmup. Typically this would be used to prepare the authentication
     * flow in an asynchronous manner. The boolean return type can be used to specify whether or not
     * the preparation could be completed.
     *
     * @return true
     */
    @Override
    public boolean prepareAuthenticationFlow(AuthenticationCallback callback) {
        // Make sure the intent can be launched by an application.
        boolean canAuthenticate = context.getPackageManager().queryIntentActivities(launchIntent, 0).size() > 0;

        if(canAuthenticate) {
            callback.onReadyToAuthenticate(this);
        }

        return canAuthenticate;
    }

    /**
     * Launches an external browser so a user can give the app access. If configured properly,
     * the user will return to the app and then {@link #handleResponse(Intent, String)}
     * can be used with {@link AuthService#authorize(Map)}
     * to obtain an Auth Token.
     * .
     *
     * @see <a href="http://soundcloud.com/you/apps">My Apps Page</a>
     */
    @Override
    public void launchAuthenticationFlow() {
        if (browserPackageName != null) {
            launchIntent.setPackage(browserPackageName);
        }
        addReferrerToIntent(launchIntent, context.getPackageName());

        context.startActivity(launchIntent);
    }

    /**
     * Resolves the package name for the browser that should open the authentication web page.
     *
     * @return the name of the application package that should open the URL or null if none is found.
     */
    private String getBrowserPackageName() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));

        List<ResolveInfo> resolveInfoList = context.getPackageManager()
                .queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // List should only contain one value if a default browser is selected
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            for (ResolveInfo info : resolveInfoList) {

                // Go to the next list item if this ResolveInfo doesn't have an associated packageName
                if (info.activityInfo == null) continue;
                if (info.activityInfo.packageName == null) continue;

                return info.activityInfo.packageName;
            }
        }

        return null;
    }
}
