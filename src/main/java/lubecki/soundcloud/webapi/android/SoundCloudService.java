package lubecki.soundcloud.webapi.android;

import java.util.HashMap;

import lubecki.soundcloud.webapi.android.models.Comment;
import lubecki.soundcloud.webapi.android.models.Comments;
import lubecki.soundcloud.webapi.android.models.Connection;
import lubecki.soundcloud.webapi.android.models.Connections;
import lubecki.soundcloud.webapi.android.models.Group;
import lubecki.soundcloud.webapi.android.models.Groups;
import lubecki.soundcloud.webapi.android.models.Playlist;
import lubecki.soundcloud.webapi.android.models.Playlists;
import lubecki.soundcloud.webapi.android.models.SecretToken;
import lubecki.soundcloud.webapi.android.models.Track;
import lubecki.soundcloud.webapi.android.models.Tracks;
import lubecki.soundcloud.webapi.android.models.User;
import lubecki.soundcloud.webapi.android.models.Users;
import lubecki.soundcloud.webapi.android.models.WebProfile;
import lubecki.soundcloud.webapi.android.models.WebProfiles;

import retrofit.Callback;
import retrofit.http.Path;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Contains methods to access the SoundCloud API.
 */
public interface SoundCloudService {

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   *                                       ~~ TRACKS ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * Returns {@link Tracks} from a given query.
   *
   * @param query The phrase by which to search for tracks.
   * @param callback Returns tracks.
   */
  @GET("/tracks") void searchTracks(@Query("q") String query, Callback<Track[]> callback);

  /**
   * Returns {@link Tracks} from a given set of query parameters.
   *
   * <ul>
   * <li>q - string to search for</li>
   * <li>tags - comma separated list of tags to search for</li>
   * <li>filter - described by {@link Track}.Filter</li>
   * <li>license - described by {@link Track}.License</li>
   * <li>bpm[from] - minimum bpm of results</li>
   * <li>bpm[to] - maximum bpm of results</li>
   * <li>duration[from] - minimum duration of results, in milliseconds</li>
   * <li>duration[to] - maximum duration of results, in milliseconds</li>
   * <li>created_at[from] - earliest date of results, format: "yyyy-mm-dd hh:mm:ss"</li>
   * <li>created_at[to] - latest date of results, format: "yyyy-mm-dd hh:mm:ss"</li>
   * <li>ids - comma separated list of tracks ids</li>
   * <li>genres - comma separated list of genres</li>
   * <li>types - comma separated list of types described by {@link Track}.Type</li>
   * </ul>
   *
   * @param queries {@link HashMap} of query params and corresponding values.
   * @param callback Returns tracks.
   */
  @GET("/tracks") void searchTracks(@QueryMap HashMap<String, String> queries,
      Callback<Track[]> callback);

  /**
   * Get a {@link Track} with a given ID.
   *
   * @param trackId ID of the track to get.
   * @param callback Returns a track.
   */
  @GET("/tracks/{id}") void getTrack(@Path("id") String trackId, Callback<Track> callback);

  /**
   * Get {@link Comments} for a given track ID.
   *
   * @param trackId ID of track.
   * @param callback Returns the list of comments..
   */
  @GET("/tracks/{id}/comments") void getTrackComments(@Path("id") String trackId,
      Callback<Comment[]> callback);

  /**
   * Get a {@link Comment} for a given track.
   *
   * @param trackId ID of track containing the comment.
   * @param commentId ID of the comment.
   * @param callback Returns the comment.
   */
  @GET("/tracks/{id}/comments/{comment-id}") void getTrackComment(@Path("id") String trackId,
      @Path("comment-id") String commentId, Callback<Comment> callback);

  /**
   * Returns a {@link Users} who favorited a track.
   *
   * @param trackId of the track to get favoriters from.
   * @param callback Returns the favoriters.
   */
  @GET("/tracks/{id}/favoriters") void getTrackFavoriters(@Path("id") String trackId,
      Callback<Users[]> callback);

  /**
   * Returns a {@link User} with a given ID who favorited a track with a given ID.
   *
   * @param trackId ID of the track to get the favoriter from.
   * @param userId ID of the user who favorited the track.
   * @param callback Returns the user who favorited the track.
   */
  @GET("/tracks/{id}/favoriters/{user-id") void getTrackFavoriter(@Path("id") String trackId,
      @Path("user-id") String userId, Callback<User> callback);

  /**
   * Returns the secret token of a track for a given track ID.
   *
   * @param trackId ID of the track that contains the secret token.
   * @param callback Returns the secret token.
   */
  @GET("/tracks/{id}/secret-token") void getTrackSecret(@Path("id") String trackId,
      Callback<SecretToken> callback);

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   *                                        ~~ USERS ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * Returns a list of {@link Users} from a given query.
   *
   * @param query The phrase by which to search for users.
   * @param callback Returns the list of users.
   */
  @GET("/users") void serachUsers(@Query("q") String query, Callback<User[]> callback);

  /**
   * Gets a {@link User} with a given ID.
   *
   * @param userId ID of the user.
   * @param callback Returns the user.
   */
  @GET("/users/{id}") void getUser(@Path("id") String userId, Callback<User> callback);

  /**
   * Returns {@link Tracks} for a user with a given ID.
   *
   * @param userId ID for the user to get tracks for.
   * @param callback Returns the tracks.
   */
  @GET("/users/{id}/tracks") void getUserTracks(@Path("id") String userId,
      Callback<Track[]> callback);

  /**
   * Returns {@link Playlists} for a user with a given ID.
   *
   * @param userId ID for the user to get playlists for.
   * @param callback Returns the playlists.
   */
  @GET("/users/{id}/playlists") void getUserPlaylists(@Path("id") String userId,
      Callback<Playlist[]> callback);

  /**
   * Returns {@link Users} followed by a user with a given ID.
   *
   * @param userId ID of the user to get the followings for.
   * @param callback Returns the followed users.
   */
  @GET("/users/{id}/followings") void getUserFollowings(@Path("id") String userId,
      Callback<User[]> callback);

  /**
   * Returns a {@link User} with a given ID followed by another user with a given ID.
   *
   * @param userId ID of the user to get list of followed users from.
   * @param followedUserId ID of the followed user.
   * @param callback Returns the followed user.
   */
  @GET("/users/{id}/followings/{following-id}") void getUserFollowing(@Path("id") String userId,
      @Path("following-id") String followedUserId, Callback<User> callback);

  /**
   * Returns {@link Users} followed by a user with a given ID.
   *
   * @param userId ID of a user to get the followers for.
   * @param callback Returns the list of users.
   */
  @GET("/users/{id}/followers") void getUserFollowers(@Path("id") String userId,
      Callback<User[]> callback);

  /**
   * Returns a {@link User} followed by a user with a given ID.
   *
   * @param userId ID of the user to get the follower for.
   * @param followerId ID of the follower.
   * @param callback Returns the follower with the specified ID.
   */
  @GET("/users/{id}/followers/{follower-id}") void getUserFollower(@Path("id") String userId,
      @Path("follower-id") String followerId, Callback<User> callback);

  /**
   * Returns {@link Comments} for a user with a given ID.
   *
   * @param userId ID of the user to get comments for.
   * @param callback Returns the comments for the user.
   */
  @GET("/users/{id}/comments") void getUserComments(@Path("id") String userId,
      Callback<Comment[]> callback);

  /**
   * Returns favorited {@link Tracks} for a user with a given ID.
   *
   * @param userId ID of the user to get favorites for.
   * @param callback Returns the favorited tracks for the user.
   */
  @GET("/users/{id}/favorites") void getUserFavorites(@Path("id") String userId,
      Callback<Track[]> callback);

  /**
   * Returns a favorited {@link Track} for a user with a given ID.
   *
   * @param userId ID of the user.
   * @param favoriteId ID of the track in the user's favorites.
   * @param callback Returns the favorite track for the user.
   */
  @GET("/users/{id}/favorites/{favorite-id}") void getUserFavorite(@Path("id") String userId,
      @Path("favorite-id") String favoriteId, Callback<Track> callback);

  /**
   * Returns a {@link Groups} that a user with a given ID is a part of.
   *
   * @param userId ID of the user.
   * @param callback Returns the user's groups.
   */
  @GET("/users/{id}/groups") void getUserGroups(@Path("id") String userId,
      Callback<Group[]> callback);

  /**
   * Returns {@link WebProfiles} that a user with a given ID is a part of.
   *
   * @param userId ID of the user.
   * @param callback Returns the user's groups.
   */
  @GET("/users/{id}/web-profiles") void getUserWebProfiles(@Path("id") String userId,
      Callback<WebProfile[]> callback);

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   *                                      ~~ PLAYLISTS ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * Returns {@link Playlists} based on a given query.
   *
   * @param query The phrase by which to search for playlists.
   * @param callback Returns the playlists.
   */
  @GET("/playlists") void getPlaylists(@Query("q") String query, Callback<Playlist[]> callback);

  /**
   * Returns {@link Playlists} based on a given query with a representation parameter.
   *
   * @param query The phrase by which to search for playlists.
   * @param representation Accepted values: "compact" or "id"
   * @param callback Returns the playlists.
   */
  @GET("/playlists") void getPlaylists(@Query("q") String query,
      @Query("representation") String representation, Callback<Playlist[]> callback);

  /**
   * Returns a secret token for a {@link Playlist}.
   *
   * @param id ID of the playlist to get the token for.
   * @param callback Returns the secret token.
   */
  @GET("/playlists/{id}/secret-token") void getPlaylistSecret(@Path("id") String id,
      Callback<SecretToken> callback);

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   * ~~ GROUPS ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * Returns {@link Groups} based on a given query.
   *
   * @param query The phrase by which to search for groups.
   * @param callback Returns the groups.
   */
  @GET("/groups") void searchGroups(@Query("q") String query, Callback<Group[]> callback);

  /**
   * Returns a {@link Group} with a given ID.
   *
   * @param id ID of the group to get.
   * @param callback Returns the group.
   */
  @GET("/groups/{id}") void getGroup(@Path("id") String id, Callback<Group> callback);

  /**
   * Returns {@link Users} that moderate a group with a given ID.
   *
   * @param id ID of the group to get moderators for.
   * @param callback Returns the moderators.
   */
  @GET("/groups/{id}/moderators") void getGroupModerators(@Path("id") String id,
      Callback<User[]> callback);

  /**
   * Returns {@link Users} that are in a group with a given ID.
   *
   * @param id ID of the group to get members for.
   * @param callback Returns the members.
   */
  @GET("/groups/{id}/members") void getGroupMembers(@Path("id") String id,
      Callback<User[]> callback);

  /**
   * Returns {@link Users} that contribute to a group with a given ID.
   *
   * @param id ID of the group to get contributors for.
   * @param callback Returns the contributors.
   */
  @GET("/groups/{id}/contributors") void getGroupContributors(@Path("id") String id,
      Callback<User[]> callback);

  /**
   * Returns all {@link Users} that are associated with a group with a given ID.
   *
   * @param id ID of the group to get all users for.
   * @param callback Returns the users.
   */
  @GET("/groups/{id}/users") void getGroupUsers(@Path("id") String id,
      Callback<User[]> callback);

  /**
   * Returns {@link Tracks} that were submitted to a group with a given ID, but have not yet been
   * approved.
   *
   * @param id ID of the group to get pending tracks for.
   * @param callback Returns the pending tracks.
   */
  @GET("/groups/{id}/pending_tracks") void getGroupPendingTracks(@Path("id") String id,
      Callback<Track[]> callback);

  /**
   * Returns a {@link Track} that was submitted to a group with a given ID, but has not yet been
   * approved.
   *
   * @param id ID of the group to get a pending track for.
   * @param trackId ID of the pending track that was submitted to a group.
   * @param callback Returns the pending tracks.
   */
  @GET("/groups/{id}/pending_tracks/{pending-id}") void getGroupPendingTrack(@Path("id") String id,
      @Path("pending-id") String trackId, Callback<Track> callback);

  /**
   * Returns {@link Tracks} that were contributed to a group with a given ID. For moderators.
   *
   * @param id ID of the group to get pending tracks for.
   * @param callback Returns the pending tracks.
   */
  @GET("/groups/{id}/contributions") void getGroupContributions(@Path("id") String id,
      Callback<Track[]> callback);

  /**
   * Returns a {@link Track} that was contributed to a group with a given ID. For moderators.
   *
   * @param id ID of the group to get a contribution for.
   * @param trackId ID of the contribution.
   * @param callback Returns the contribution.
   */
  @GET("/groups/{id}/pending_tracks/{contribution-id}") void getGroupContribution(
      @Path("id") String id, @Path("contribution-id") String trackId, Callback<Track> callback);


  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   *
   *                                          ~~ Me ~~
   *
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  /**
   * Gets the authenticated {@link User}.
   *
   * @param callback Returns the user.
   */
  @GET("/me") void getMe(Callback<User> callback);

  /**
   * Returns {@link Tracks} for the authenticated user.
   *
   * @param callback Returns the tracks.
   */
  @GET("/me/tracks") void getMyTracks(Callback<Track[]> callback);

  /**
   * Returns {@link Playlists} for the authenticated user.
   *
   * @param callback Returns the playlists.
   */
  @GET("/me/playlists") void getMyPlaylists(Callback<Playlist[]> callback);

  /**
   * Returns {@link Users} followed by the authenticated user.
   *
   * @param callback Returns the followed users.
   */
  @GET("/me/followings") void getMyFollowings(Callback<User> callback);

  /**
   * Returns a {@link User} followed by the authenticated user.
   *
   * @param followedUserId ID of the followed user.
   * @param callback Returns the followed user.
   */
  @GET("/me/followings/{following-id}") void getMyFollowing(
      @Path("following-id") String followedUserId, Callback<User> callback);

  /**
   * Returns {@link Users} followed by the authenticated user.
   *
   * @param callback Returns the list of users.
   */
  @GET("/me/followers") void getMyFollowers(Callback<User[]> callback);

  /**
   * Returns a {@link User} followed by the authenticated user.
   *
   * @param followerId ID of the follower.
   * @param callback Returns the follower with the specified ID.
   */
  @GET("/me/followers/{follower-id}") void getMyFollower(@Path("follower-id") String followerId,
      Callback<User> callback);

  /**
   * Returns {@link Comments} for the authenticated user.
   *
   * @param callback Returns the comments for the user.
   */
  @GET("/me/comments") void getMyComments(Callback<Comment[]> callback);

  /**
   * Returns favorited {@link Tracks} for the authenticated user.
   *
   * @param callback Returns the favorited tracks for the user.
   */
  @GET("/me/favorites") void getMyFavorites(Callback<Track[]> callback);

  /**
   * Returns a favorited {@link Track} for the authenticated user.
   *
   * @param favoriteId ID of the track in the user's favorites.
   * @param callback Returns the favorite track for the user.
   */
  @GET("/me/favorites/{favorite-id}") void getMyFavorite(@Path("favorite-id") String favoriteId,
      Callback<Track> callback);

  /**
   * Returns a list of groups that the authenticated user is a part of.
   *
   * @param callback Returns the user's groups.
   */
  @GET("/me/groups") void getMyGroups(Callback<Group[]> callback);

  /**
   * Returns a list of web profiles that the authenticated user has.
   *
   * @param callback Returns the user's web profiles.
   */
  @GET("/me/web-profiles") void getMyWebProfiles(Callback<WebProfile[]> callback);

  /**
   * Returns {@link Connections} for the authenticated user.
   *
   * @param callback Returns the connections.
   */
  @GET("/me/connections") void getMyConnections(Callback<Connection[]> callback);

  /**
   * Returns a {@link Connection} for the authenticated user.
   *
   * @param connectionId ID of the connection.
   * @param callback Returns the connection.
   */
  @GET("/me/connections") void getMyConnection(String connectionId, Callback<Connection> callback);
}
