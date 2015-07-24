package lubecki.soundcloud.webapi.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Representation of a SoundCloud user.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#users">SoundCloud User
 * Reference</a>
 */
public class User {

  /**
   * Integer value of a user's ID.
   */
  public String id;

  public String permalink;

  public String username;

  public String uri;

  public String permalink_url;

  public String avatar_url;

  public String country;

  public String full_name;

  public String city;

  public String description;

  @SerializedName("discogs-name") public String discogs_name;

  @SerializedName("myspace-name") public String myspace_name;

  public String website;

  @SerializedName("website-tile") public String website_title;

  @SerializedName("online") public boolean is_online;

  /**
   * Integer value of the number of public tracks a user has.
   */
  public String track_count;

  /**
   * Integer value of the number of public playlists a user has.
   */
  public String playlist_count;

  /**
   * Integer value of the  number of followers a user has.
   */
  public String followers_count;

  /**
   * Integer value of the number of users a user follows.
   */
  public String followings_count;

  public String public_favorites_count;

  /**
   * Binary data of user avatar. Only for uploading.
   */
  public String avatar_data;
}
