# SoundCloud Web API for Android

This project is a wrapper for the [SoundCloud Web API](https://developers.soundcloud.com/docs/api/reference). It uses [Retrofit](http://square.github.io/retrofit/) to create Java interfaces from API endpoints.

## Building
This project is built using [Gradle](https://gradle.org/):

1. Clone the repository: `git clone https://github.com/lubecjac/soundcloud-web-api-android.git`
2. Build: `./gradlew assemble`
3. Grab the `aar` that can be found in `soundcloud-web-api-android/build/outputs/aar/soundcloud-web-api-android-release.aar` and put it in the `libs` folder in your application

## Integration into your project

This project depends on `Retrofit 1.9.0` and `OkHttp 2.4.0`. When you build it, it creates an `aar` that doesn't contain Retrofit and OkHttp files. To make your app work you'll need to include these dependencies in your app's `build.gradle` file.

Add following to the `build.gradle` file in your app:

```groovy
repositories {
    mavenCentral()
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile(name:'soundcloud-web-api-android-release', ext:'aar')
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.squareup.okhttp:okhttp:2.4.0'

    // Other dependencies
}
```

The `repositories` section tells your application to look for the `soundcloud-web-api-android-release.aar`
in the local repository in the `libs` folder.



## Usage

Out of the box, it uses the default [OkHttp](http://square.github.io/okhttp/) client.

```java
SoundCloudAPI api = new SoundCloudAPI();

// Few of the SoundCloud Web API endpoints require authorization.
// If you know you'll require authorization you can add this step.
api.setToken("token");

SoundCloudService soundcloud = api.getService();

soundcloud.getTrack("trackId", new Callback<Track>() {
    @Override
    public void success(Album album, Response response) {
        Log.d("Track success", track.title);
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("Track failure", error.toString());
    }
});
```

It is also possible to construct the adapter with custom parameters.

```java
final String accessToken = "myAccessToken";

RestAdapter adapter = new RestAdapter.Builder()
        .setEndpoint(SoundCloudAPI.SOUNDCLOUD_API_ENDPOINT)
        .setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("oauth_token", accessToken);
            }
        })
        .build();

SoundCloudService soundcloud = adapter.create(SoundCloudService.class);
```

## Obtaining Access Tokens

The provided SoundCloudAuthenticator class makes obtaining an auth token easy.

To open login:

```java
SoundCloudAuthenticator.openLoginActivity(context, clientId, clientSecret);
```
Login will open in the default phone browser. Once the user has given the application permission to authenticate on their behalf, SoundCloud will redirect to the URI specified by the app. This intent should be filtered by the activity that should obtain the Auth Token.

In the android manifest, a redirect of `yourapp://soundcloud/redirect` would look like:

```xml
<activity android:name=".YourActivity">
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

Once the user has completed the external auth and the intent filter reopens the app, the activity should override `protected void onNewIntent(Intent intent)` and check that the data in the intent matches the specified redirect URI. If it does, the app should use the SoundCloudAuthenticator to handle the response and then obtain the auth token.

```java
@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    
    if(intent.getDataString().startsWith("redirect")) {
        Authenticator auth = 
            SoundCloudAuthenticator.handleResponse(intent, "redirect", "clientId", "secret"); 
            
        SoundCloudAuthenticator.AuthService service = SoundCloudAuthenticator.getAuthService();
        service.authorize(auth, new Callback<AuthenticationResponse>() {
            @Override
            public void success(AuthenticationResponse authResponse, Response response) {
                Log.v("AUTH SUCCESS", "TOKEN: " + authResponse.access_token);
                // Save the token
            }

            @Override 
            public void failure(RetrofitError error) {
                Log.e("AUTH FAILED", "ERROR: " + error.getMessage());
                // See what went wrong
            }
        });
    } else {
        // handle other new intents    
    }
}
```

For information on client ID, secret, and the redirect URI, see the [SoundCloud Apps Page](http://soundcloud.com/you/apps). 

## Error Handling

When using Retrofit, errors are returned as [`RetrofitError`](http://square.github.io/retrofit/javadoc/retrofit/RetrofitError.html) objects. These objects contain HTTP status codes and their descriptions, for example `400 - Bad Request`.

## Help

#### Bugs, Feature requests
Found a bug? Something that's missing? Feedback is an important part of improving the project, so please [open an issue](https://github.com/lubecjac/soundcloud-web-api-android/issues).

#### Code
Fork this project and start working on your own feature branch. When you're done, send a Pull Request to have your suggested changes merged into the master branch by the project's collaborators. Read more about the [GitHub flow](https://guides.github.com/introduction/flow/).

## Unavailable Resources and TODO

- All http PUT and DELETE requests
  - Poor / nonexistent documentation.
  
- /me/activities and related subresources
  - List of all activities: playlists, songs, etc.
  - Different types under same name in JSON -- "collection".
  - Will try to add soon with custom (de)serialization.
  
- /apps
  - List of apps on the SoundCloud apps page.
  - Not useful in the scope of Birdcage.
  
- /resolve
  - Resolves SoundCloud web urls to API endpoints.
  - Not useful in the scope of Birdcage.
  
- /oembed
  - Creates objects that can be embedded in a web page.
  - Not useful in the scope of Birdcage.
  
- /comments
  - No officially documented endpoints.