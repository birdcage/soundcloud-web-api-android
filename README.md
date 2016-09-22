# SoundCloud Web API for Android

This project is a wrapper for the [SoundCloud Web API](https://developers.soundcloud.com/docs/api/reference).
It uses [Retrofit](http://square.github.io/retrofit/) to create Java interfaces from API endpoints.

## Integration into your project

In the main `build.gradle` file:

```groovy
repositories {
    jcenter() // For production builds.
    maven {
      url 'http://oss.jfrog.org/artifactory/oss-snapshot-local' // For snapshots.
    }
}
```

Snapshots can be found on [JFrog's Open Source Artifactory Server](https://oss.jfrog.org/libs-snapshot/com/jlubecki/soundcloud/soundcloud-api/).

Add following to the `build.gradle` file in your app module:

```groovy
dependencies {
    compile 'com.jlubecki.soundcloud:soundcloud-api:1.2.1'

    // Other dependencies
}
```

Alternatively, the project can be imported into an existing project as a module.


## Usage

### Making Requests

```java
SoundCloudAPI api = new SoundCloudAPI("clientId");

// Few of the SoundCloud Web API endpoints require authorization.
// If you know you'll require authorization you can add this step.
api.setToken("token");

SoundCloudService soundcloud = api.getService();

soundcloud.getTrack("trackId").enqueue(new Callback<Track>() {
    @Override
    public void onResponse(Call<Track> call, Response<Track> response) {
        Track track = response.body();
        
        if(track != null) {
            Log.i(TAG, "Track success: " + track.title);
        } else {
            Log.w(TAG, "Error in response.");
        }
    }

    @Override
    public void onFailure(Call<Track> call, Throwable t) {
        Log.e(TAG, "Error getting track.", t);
    }
});
```

### Authentication

The provided implementations of the SoundCloudAuthenticator class make 
obtaining an auth token easy.

To setup login:

```java

private AuthenticationStrategy strategy;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // ...
    
    AuthTabServiceConnection serviceConnection = new AuthTabServiceConnection(new AuthenticationCallback() { ... });

    ChromeTabsSoundCloudAuthenticator tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);
    BrowserSoundCloudAuthenticator browserAuthenticator = new BrowserSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);
    WebViewSoundCloudAuthenticator webViewAuthenticator = new WebViewSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, REQUEST_CODE_AUTHENTICATE);
    
    // You need to create a strategy in `onCreate` to make sure it is always available to handle new Intent data in `onResume`
    strategy = new AuthenticationStrategy.Builder(MainActivity.this)
            .addAuthenticator(tabsAuthenticator)
            .addAuthenticator(browserAuthenticator)
            .addAuthenticator(webViewAuthenticator)
            .setCheckNetwork(true)
            .onFailure(new AuthenticationStrategy.OnNetworkFailureListener() {
                @Override
                public void onFailure(Throwable throwable) {
                    // Couldn't connect to the internet...
                }
            })
            .build();
            
    strategy.beginAuthentication(new AuthenticationCallback() {
        @Override
        public void onReadyToAuthenticate(SoundCloudAuthenticator authenticator) {
            authenticator.launchAuthenticationwhereFlow(); // launch immediately
            // ... or ...
            MyActivity.this.authenticator = authenticator; // save to launch when ready
        }
    });
```

Once the user has given the application permission to authenticate on 
their behalf, SoundCloud will redirect to the URI specified by the app. 
This intent should be filtered by the activity that should obtain the 
Auth Token. Additionally, that activity should be a single top activity 
meaning only one instance of it exists at a given time. To do this, add 
`android:launchMode="singleTop"` to the activity in the manifest.

In the android manifest, a redirect of `yourapp://soundcloud/redirect` 
would look like:

```xml
<activity android:name=".YourActivity"
  android:launchMode="singleTop">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        
        <data android:scheme="yourapp" 
            android:host="soundcloud" 
            android:pathPrefix="/redirect"/>
    </intent-filter>
</activity>
```

#### Handling the Intent Filter

A sample implementation of code to handle an intent.

```java
void getTokenFromIntent(Intent intent) {
    if (strategy.canAuthenticate(intent)) {
        strategy.getToken(intent, CLIENT_SECRET, new AuthenticationStrategy.ResponseCallback() {
            @Override
            public void onAuthenticationResponse(AuthenticationResponse response) {
                switch (response.getType()) {
                    case TOKEN:
                        Log.i(TAG, "Token: " + response.access_token); // save token
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
```

A few changes to the activity lifecycle to handle incoming intents:

```java
@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
}

// Always called after `onNewIntent`, if not using ChromeTabs, you can
// call `getTokenFromIntent` in `onNewIntent`.
@Override
protected void onResume() {
    super.onResume();
    getTokenFromIntent(getIntent());
}

// Use with `WebViewSoundCloudAuthenticator`.
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == YOUR_REQUEST_CODE) {
        getTokenFromIntent(data);
    }
}
```

#### Browser Based Notes

Once the user has completed the external auth and the intent filter reopens the app, the activity
should override `protected void onNewIntent(Intent intent)` and check that the data in the intent
matches the specified redirect URI. If it does, the app should use the SoundCloudAuthenticator to
handle the response and then obtain the auth token.

#### Chrome Custom Tabs Notes

Using Chrome Custom Tabs will prevent `onNewIntent` from being called. The activity should still
receive a new Intent, but it can be handled in `onResume` instead. Handling the intent in this
case can be a bit more complicated, but some implementations may see advantages in using Chrome
Custom Tabs to authenticate.

For instance:
- Customize look and feel of tabs using `tabsAuthenticator.newTabsIntentBuilder()` 
and `tabsAuthenticator.setTabsIntentBuilder(builder)`.
- Improve loading times of the authentication flow (built in to the `AuthTabServiceConnection`).
- Add callbacks to observe simple navigation events to understand the authentication flow.

#### WebView Based Notes

The WebView authenticator is possibly the easiest to use. However, you expose yourself to 
security vulnerabilities because it enables javascript (i.e. XSS attacks). A potential upside is
that the WebView will never try to open the SoundCloud app during the authentication (if it does, 
open an issue).


### Other Info

For information on client ID, secret, and the redirect URI, see the [SoundCloud Apps Page](http://soundcloud.com/you/apps). 

## License

This project is distributed as open source software under the MIT License. Please see the LICENSE file for more info.

## Help

#### Bugs, Feature requests
Found a bug? Something that's missing? Feedback is an important part of improving the project, so
please [open an issue](https://github.com/birdcage/soundcloud-web-api-android/issues).

#### Code
Fork this project and start working on your own feature branch. When you're done, send a Pull Request
to have your suggested changes merged into the master branch by the project's collaborators.
Read more about the [GitHub flow](https://guides.github.com/introduction/flow/).

## Unavailable Resources and TODO

- All http PUT and DELETE requests
  - Poor / nonexistent documentation.
  
- /me/activities and related subresources
  - List of all activities: playlists, songs, etc.
  - Different types under same name in JSON -- "collection".
  - Will try to add soon with custom (de)serialization.
  
- /apps
  - List of apps on the SoundCloud apps page.
  
- /resolve
  - Resolves SoundCloud web urls to API endpoints.
  
- /oembed
  - Creates objects that can be embedded in a web page.
  
- /comments
  - No officially documented endpoints.