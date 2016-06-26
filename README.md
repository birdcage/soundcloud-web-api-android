# SoundCloud Web API for Android

This project is a wrapper for the [SoundCloud Web API](https://developers.soundcloud.com/docs/api/reference).
It uses [Retrofit](http://square.github.io/retrofit/) to create Java interfaces from API endpoints.

## Integration into your project

Add following to the `build.gradle` file in your app:

```groovy
dependencies {
    compile 'com.jlubecki.soundcloud:soundcloud-api:1.1.1'

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


It is also possible to construct the adapter with custom parameters.

```java
final String clientId = "id";
final String accessToken = "token";

OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new Interceptor() {
        @Override 
        public Response intercept(Chain chain) throws IOException {
             Request request = chain.request();
        
             HttpUrl url = request.url()
                 .newBuilder()
                 .addEncodedQueryParameter("client_id", clientId)
                 .addEncodedQueryParameter("token", accessToken)
                 .build();
        
             Request newRequest = request.newBuilder()
                 .url(url)
                 .build();
        
             return chain.proceed(newRequest);
        }
    })
    .build();

Retrofit adapter = new Retrofit.Builder()
    .client(client)
    .baseUrl(SoundCloudAPI.SOUNDCLOUD_API_ENDPOINT)
    .addConverterFactory(GsonConverterFactory.create())
    .build();

service = adapter.create(SoundCloudService.class);

SoundCloudService soundcloud = adapter.create(SoundCloudService.class);
```

### Authentication

The provided implementations of the SoundCloudAuthenticator class make 
obtaining an auth token easy.

To setup login:

```java

private AuthenticationCallback authCallback = new AuthenticationCallback() {
        @Override
        public void onReadyToAuthenticate() {
            // Tabs Authenticator is ready when this is called
        }
    
        @Override
        public void onAuthenticationEnded() {
            Log.i(TAG, "Auth ended.");
        }
    };

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    ...

    // Prepare browser auth, ready to launch instantly
    browserAuthenticator = new BrowserSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this);

    // or...
    
    // Prepare Chrome Tabs auth, ready to launch based on authCallback
    AuthTabServiceConnection serviceConnection = new AuthTabServiceConnection(authCallback);
    tabsAuthenticator = new ChromeTabsSoundCloudAuthenticator(CLIENT_ID, REDIRECT, this, serviceConnection);
}

@Override 
protected void onStart() {
    super.onStart();

    // The documentation for Chrome Custom Tabs recommends preparing the
    // Custom Tab in onStart.
    
    if(tabsAuthenticator != null) {
        boolean ok = tabsAuthenticator.prepareAuthenticationFlow();

        Log.i(TAG, "Tab auth did connect: " + ok);
    }
}
```

When ready to launch the authentication flow:

```java
browserAuthenticator.launchAuthenticationFlow();

// or...

tabsAuthenticator.launchAuthenticationFlow();
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
void authenticateWithIntent(Intent intent) {
    super.onNewIntent(intent);
    
    if(intent.getDataString().startsWith("redirect")) {
        HashMap<String, String> authMap = 
            SoundCloudAuthenticator.handleResponse(intent, "redirect", "clientId", "secret"); 
            
        SoundCloudAuthenticator.AuthService service = someAuthenticator.getAuthService();
        service.authorize(authMap).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                // See if the response succeeded and has a token
            }

            @Override 
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) 
                // See what went wrong
            }
        });
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