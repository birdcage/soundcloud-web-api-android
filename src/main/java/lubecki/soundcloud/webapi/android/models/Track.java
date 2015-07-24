package lubecki.soundcloud.webapi.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import lubecki.soundcloud.webapi.android.SoundCloudService;
import retrofit.Callback;

/**
 * Representation of a SoundCloud track.
 *
 * Contains:
 * <ul>
 * <li>{@link Type}</li>
 * <li>{@link Filter}</li>
 * <li>{@link License}</li>
 * </ul>
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#tracks">
 *   SoundCloud Track Reference</a>
 */
public class Track {

  /**
   * ~~ Fields ~~
   */

  /**
   * Integer value of a track's ID.
   */
  public String id;

  public String created_at;

  public String userid;

  public MiniUser user;

  public String title;

  public String permalink;

  public String permalink_url;

  public String uri;

  public String sharing;

  /**
   * Possible values:
   * <ul>
   * <li>{@link EmbeddableBy#ALL}</li>
   * <li>{@link EmbeddableBy#ME}</li>
   * <li>{@link EmbeddableBy#NONE}</li>
   * </ul>
   *
   * @see EmbeddableBy
   */
  public String embeddable_by;

  public String purchase_url;

  /**
   * JPEG, PNG and GIF are accepted when uploading and will be encoded to multiple JPEGs in these
   * formats:
   *
   * <ul>
   * <li>t500x500: 500×500</li>
   * <li>crop: 400×400</li>
   * <li>t300x300: 300×300</li>
   * <li>large: 100×100 (default)</li>
   * <li>t67x67: 67×67 (only on artworks)</li>
   * <li>badge: 47×47</li>
   * <li>tiny: 20×20 (on artworks)</li>
   * <li>tiny: 18×18 (on avatars)</li>
   * <li>mini: 16×16</li>
   * </ul>
   *
   * The URL is pointing to the format large by default.
   * If you want to use a different format you have to replace large
   * with the specific format name in the image URL.
   *
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#artwork_url">Album Artwork
   * Reference</a>
   */
  public String artwork_url;

  public String description;

  /**
   * Length of track in milliseconds.
   */
  public String duration;

  public String genre;

  /**
   * The tag_list property contains a list of tags separated by spaces.
   * Multiword tags are quoted in doublequotes.
   * Machine tags that follow the pattern NAMESPACE:KEY=VALUE are supported.
   * For example:
   *
   * <ul>
   * <li>geo:lat=43.555</li>
   * <li>camel:size=medium</li>
   * <li>“machine:tag=with space”</li>
   * </ul>
   *
   * Machine tags are not revealed to the user on the track pages.
   */
  public String tags_list;

  //TODO MiniLabel object for "label" field if necessary, no "label" field returned by /tracks api request though

  /**
   * Integer value of the label's ID.
   */
  public String label_id;

  public String label_name;

  /**
   * Integer release number.
   */
  public String release;

  public String release_day;

  public String release_month;

  public String release_year;

  @SerializedName("streamable") public boolean is_streamable;

  @SerializedName("downloadable") public boolean is_downloadable;

  /**
   * Possible values:
   * <ul>
   * <li>{@link State#FAILED}</li>
   * <li>{@link State#FINISHED}</li>
   * <li>{@link State#PROCESSING}</li>
   * </ul>
   *
   * @see State
   */
  public String state;

  /**
   * Possible values:
   * <ul>
   * <li>{@link License#ALL_RIGHTS_RESERVED}</li>
   * <li>{@link License#NO_RIGHTS_RESERVED}</li>
   * <li>{@link License#CC_ATTRIBUTION}</li>
   * <li>{@link License#CC_ATTRIBUTION_NO_DERIVATIVES}</li>
   * <li>{@link License#CC_ATTRIBUTION_NONCOMMERCIAL}</li>
   * <li>{@link License#CC_ATTRIBUTION_SHARE_ALIKE}</li>
   * <li>{@link License#CC_ATTRIBUTION_NONCOMMERCIAL_NO_DERIVATES}</li>
   * <li>{@link License#CC_ATTRIBUTION_NONCOMMERCIAL_SHARE_ALIKE}</li>
   * </ul>
   *
   * @see License
   */
  public String license;

  /**
   * Possible values:
   * <ul>
   * <li>{@link Type#ORIGINAL}</li>
   * <li>{@link Type#REMIX}</li>
   * <li>{@link Type#LIVE}</li>
   * <li>{@link Type#RECORDING}</li>
   * <li>{@link Type#SPOKEN}</li>
   * <li>{@link Type#PODCAST}</li>
   * <li>{@link Type#DEMO}</li>
   * <li>{@link Type#IN_PROGRESS}</li>
   * <li>{@link Type#STEM}</li>
   * <li>{@link Type#LOOP}</li>
   * <li>{@link Type#SOUND_EFFECT}</li>
   * <li>{@link Type#SAMPLE}</li>
   * <li>{@link Type#OTHER}</li>
   * </ul>
   *
   * @see Type
   */
  public String track_type;

  public String waveform_url;

  public String download_url;

  public String stream_url;

  public String video_url;

  public String bpm;

  public boolean commentable;

  public String isrc;

  public String key_signature;

  public String comment_count;

  public String download_count;

  public String playback_count;

  public String favoritings_count;

  public String original_format;

  /**
   * Size in bytes of the uploaded file.
   */
  public String original_file_size;

  /**
   * Mini representation of the user and the app that was used to create a sound. Only defined if
   * a track was published using an app in the <a href="http://soundcloud.com/apps">App
   * Gallery</a>.
   */
  public CreatorApp created_with;

  /**
   * Only for uploading. Binary data of the audio file.
   */
  public String asset_data;

  /**
   * Only for uploading. Binary data of the album artwork.
   */
  public String artwork_data;

  /**
   * Authenticated requests only. Whether or not track is favorited by current user.
   */
  public boolean user_favorite;

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   *                                    ~~ Possible Values ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * <ul>
   * <li>ALL_RIGHTS_RESERVED - no sharing</li>
   * <li>NO_RIGHTS_RESERVED - share with everyone</li>
   * <li>ATTRIBUTION - Copy, distribution, etc. allowed if proper attribution is given</li>
   * <li>NONCOMMERCIAL - Distribute, display, etc. allowed for noncommercial use</li>
   * <li>NO_DERIVATIVES - Copy, distribution, etc. allowed for the original work only</li>
   * <li>
   *   SHARE_ALIKE - Allow distribution of derivative works under license identical to original
   * </li>
   * </ul>
   *
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#license">License
   * Reference</a>
   * @see
   * <a href="http://help.soundcloud.com/customer/portal/articles/243852-what-is-creative-commons-">
   *   CreativeCommons Reference</a>
   */
  public static class License {
    public static final String ALL_RIGHTS_RESERVED = "all-rights-reserved";
    public static final String NO_RIGHTS_RESERVED = "no-rights-reserved";
    public static final String CC_ATTRIBUTION = "cc-by";
    public static final String CC_ATTRIBUTION_NONCOMMERCIAL = "cc-by-nc";
    public static final String CC_ATTRIBUTION_NO_DERIVATIVES = "cc-by-nd";
    public static final String CC_ATTRIBUTION_SHARE_ALIKE = "cc-by-sa";
    public static final String CC_ATTRIBUTION_NONCOMMERCIAL_NO_DERIVATES = "cc-by-nc-nd";
    public static final String CC_ATTRIBUTION_NONCOMMERCIAL_SHARE_ALIKE = "cc-by-nc-sa";
  }

  /**
   * Describes the type of sound for a given track.
   *
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#track_type">Track Type
   * Reference</a>
   */
  public static class Type {
    public static final String ORIGINAL = "original";
    public static final String REMIX = "remix";
    public static final String LIVE = "live";
    public static final String RECORDING = "recording";
    public static final String SPOKEN = "spoken";
    public static final String PODCAST = "podcast";
    public static final String DEMO = "demo";
    public static final String IN_PROGRESS = "in progress";
    public static final String STEM = "stem";
    public static final String LOOP = "loop";
    public static final String SOUND_EFFECT = "sound effect";
    public static final String SAMPLE = "sample";
    public static final String OTHER = "other";
  }

  /**
   * Describes a track's processing state for a given track.
   *
   * @see <a href="https://developers.soundcloud.com/docs/api/reference#state">State Reference</a>
   */
  public static class State {
    public static final String PROCESSING = "processing";
    public static final String FAILED = "failed";
    public static final String FINISHED = "finished";
  }

  /**
   * Only for use with {@link SoundCloudService#searchTracks(HashMap, Callback)}.
   * Filters tracks by visibility on site.
   */
  public static class Filter {
    public static final String ALL = "all";
    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
  }

  /**
   * Describes who has permission to embed a track.
   */
  public static class EmbeddableBy {
    public static final String ALL = "all";
    public static final String ME = "me";
    public static final String NONE = "none";
  }
}
