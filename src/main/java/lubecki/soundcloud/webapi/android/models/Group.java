package lubecki.soundcloud.webapi.android.models;

/**
 * Representation of a SoundCloud Group.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#groups">
 *   SoundCloud Group Reference</a>
 */
public class Group {
  
  private String id;

  private String created_at;
  
  private String permalink;
  
  private String name;

  private String short_description;
  
  private String description;
  
  private String uri;
  
  private String artwork_url;
  
  private String permalink_url;
  
  private MiniUser creator;
}
