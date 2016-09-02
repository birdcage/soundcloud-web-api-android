/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Jacob Lubecki
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

package com.jlubecki.soundcloud.webapi.android;

import com.jlubecki.soundcloud.webapi.android.models.Comment;
import com.jlubecki.soundcloud.webapi.android.models.Comments;
import com.jlubecki.soundcloud.webapi.android.models.Connection;
import com.jlubecki.soundcloud.webapi.android.models.Connections;
import com.jlubecki.soundcloud.webapi.android.models.Group;
import com.jlubecki.soundcloud.webapi.android.models.Groups;
import com.jlubecki.soundcloud.webapi.android.models.Playlist;
import com.jlubecki.soundcloud.webapi.android.models.SecretToken;
import com.jlubecki.soundcloud.webapi.android.models.Track;
import com.jlubecki.soundcloud.webapi.android.models.User;
import com.jlubecki.soundcloud.webapi.android.models.Users;
import com.jlubecki.soundcloud.webapi.android.models.WebProfile;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Contains methods to access the SoundCloud API.
 */
@SuppressWarnings("unused") //
public interface SoundCloudService {

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     *                                       ~~ TRACKS ~~
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Returns a list of {@link Track} items from a given query.
     *
     * @param query The phrase by which to search for tracks.
     * @return The call that can be used to get the data.
     */
    @GET("tracks")
    Call<List<Track>> searchTracks(@Query("q") String query);

    /**
     * Returns a list of {@link Track} items from a given set of query parameters.
     * <p/>
     * <ul>
     * <li>q - string to search for</li>
     * <li>tags - comma separated list of tags to search for</li>
     * <li>filter - described by Track.Filter</li>
     * <li>license - described by Track.License</li>
     * <li>bpm[from] - minimum bpm of results</li>
     * <li>bpm[to] - maximum bpm of results</li>
     * <li>duration[from] - minimum duration of results, in milliseconds</li>
     * <li>duration[to] - maximum duration of results, in milliseconds</li>
     * <li>created_at[from] - earliest date of results, format: "yyyy-mm-dd hh:mm:ss"</li>
     * <li>created_at[to] - latest date of results, format: "yyyy-mm-dd hh:mm:ss"</li>
     * <li>ids - comma separated list of tracks ids</li>
     * <li>genres - comma separated list of genres</li>
     * <li>types - comma separated list of types described by Track.Type</li>
     * </ul>
     *
     * @param queries {@link HashMap} of query params and corresponding values.
     * @return The call that can be used to get the data.
     */
    @GET("tracks")
    Call<List<Track>> searchTracks(@QueryMap HashMap<String, String> queries);

    /**
     * Get a {@link Track} with a given ID.
     *
     * @param trackId ID of the track to get.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id") String trackId);

    /**
     * Get {@link Comments} for a given track ID.
     *
     * @param trackId ID of track.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}/comments")
    Call<List<Comment>> getTrackComments(@Path("id") String trackId);

    /**
     * Get a {@link Comment} for a given track.
     *
     * @param trackId   ID of track containing the comment.
     * @param commentId ID of the comment.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}/comments/{comment-id}")
    Call<Comment> getTrackComment(
            @Path("id") String trackId, @Path("comment-id") String commentId);

    /**
     * Returns a {@link Users} who favorited a track.
     *
     * @param trackId of the track to get favoriters from.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}/favoriters")
    Call<List<User>> getTrackFavoriters(@Path("id") String trackId);

    /**
     * Returns a {@link User} with a given ID who favorited a track with a given ID.
     *
     * @param trackId ID of the track to get the favoriter from.
     * @param userId  ID of the user who favorited the track.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}/favoriters/{user-id")
    Call<User> getTrackFavoriter(@Path("id") String trackId,
                                 @Path("user-id") String userId);

    /**
     * Returns the secret token of a track for a given track ID.
     *
     * @param trackId ID of the track that contains the secret token.
     * @return The call that can be used to get the data.
     */
    @GET("tracks/{id}/secret-token")
    Call<SecretToken> getTrackSecret(@Path("id") String trackId);

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
     * @return The call that can be used to get the data.
     */
    @GET("users")
    Call<List<User>> searchUsers(@Query("q") String query);

    /**
     * Gets a {@link User} with a given ID.
     *
     * @param userId ID of the user.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}")
    Call<User> getUser(@Path("id") String userId);

    /**
     * Returns a list of {@link Track} items for a user with a given ID.
     *
     * @param userId ID for the user to get tracks for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/tracks")
    Call<List<Track>> getUserTracks(@Path("id") String userId);

    /**
     * Returns a list of {@link Playlist} objects for a user with a given ID.
     *
     * @param userId ID for the user to get playlists for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/playlists")
    Call<List<Playlist>> getUserPlaylists(@Path("id") String userId);

    /**
     * Returns {@link Users} followed by a user with a given ID.
     *
     * @param userId ID of the user to get the followings for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/followings")
    Call<List<User>> getUserFollowings(@Path("id") String userId);

    /**
     * Returns a {@link User} with a given ID followed by another user with a given ID.
     *
     * @param userId         ID of the user to get list of followed users from.
     * @param followedUserId ID of the followed user.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/followings/{following-id}")
    Call<User> getUserFollowing(
            @Path("id") String userId, @Path("following-id") String followedUserId);

    /**
     * Returns {@link Users} followed by a user with a given ID.
     *
     * @param userId ID of a user to get the followers for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/followers")
    Call<List<User>> getUserFollowers(@Path("id") String userId);

    /**
     * Returns a {@link User} followed by a user with a given ID.
     *
     * @param userId     ID of the user to get the follower for.
     * @param followerId ID of the follower.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/followers/{follower-id}")
    Call<User> getUserFollower(@Path("id") String userId,
                               @Path("follower-id") String followerId);

    /**
     * Returns {@link Comments} for a user with a given ID.
     *
     * @param userId ID of the user to get comments for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/comments")
    Call<List<Comment>> getUserComments(@Path("id") String userId);

    /**
     * Returns a favorited list of {@link Track} items for a user with a given ID.
     *
     * @param userId ID of the user to get favorites for.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/favorites")
    Call<List<Track>> getUserFavorites(@Path("id") String userId);

    /**
     * Returns a favorited {@link Track} for a user with a given ID.
     *
     * @param userId     ID of the user.
     * @param favoriteId ID of the track in the user's favorites.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/favorites/{favorite-id}")
    Call<Track> getUserFavorite(@Path("id") String userId,
                                @Path("favorite-id") String favoriteId);

    /**
     * Returns a {@link Groups} that a user with a given ID is a part of.
     *
     * @param userId ID of the user.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/groups")
    Call<List<Group>> getUserGroups(@Path("id") String userId);

    /**
     * Returns a list of {@link WebProfile} objects that describe a user with a given ID.
     *
     * @param userId ID of the user.
     * @return The call that can be used to get the data.
     */
    @GET("users/{id}/web-profiles")
    Call<List<WebProfile>> getUserWebProfiles(@Path("id") String userId);

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     *                                      ~~ PLAYLISTS ~~
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Returns a list of {@link Playlist} items based on a given query.
     *
     * @param query The phrase by which to search for playlists.
     * @return The call that can be used to get the data.
     */
    @GET("playlists")
    Call<List<Playlist>> getPlaylists(@Query("q") String query);

    /**
     * Returns a list of {@link Playlist} items based on a given query with a representation parameter.
     *
     * @param query          The phrase by which to search for playlists.
     * @param representation Accepted values: "compact" or "id"
     * @return The call that can be used to get the data.
     */
    @GET("playlists")
    Call<List<Playlist>> getPlaylists(@Query("q") String query, @Query("representation") String representation);

    /**
     * Returns a secret token for a {@link Playlist}.
     *
     * @param id ID of the playlist to get the token for.
     * @return The call that can be used to get the data.
     */
    @GET("playlists/{id}/secret-token")
    Call<SecretToken> getPlaylistSecret(@Path("id") String id);

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
     * @return The call that can be used to get the data.
     */
    @GET("groups")
    Call<List<Group>> searchGroups(@Query("q") String query);

    /**
     * Returns a {@link Group} with a given ID.
     *
     * @param id ID of the group to get.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}")
    Call<Group> getGroup(@Path("id") String id);

    /**
     * Returns {@link Users} that moderate a group with a given ID.
     *
     * @param id ID of the group to get moderators for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/moderators")
    Call<List<User>> getGroupModerators(@Path("id") String id);

    /**
     * Returns {@link Users} that are in a group with a given ID.
     *
     * @param id ID of the group to get members for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/members")
    Call<List<User>> getGroupMembers(@Path("id") String id);

    /**
     * Returns {@link Users} that contribute to a group with a given ID.
     *
     * @param id ID of the group to get contributors for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/contributors")
    Call<List<User>> getGroupContributors(@Path("id") String id);

    /**
     * Returns all {@link Users} that are associated with a group with a given ID.
     *
     * @param id ID of the group to get all users for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/users")
    Call<List<User>> getGroupUsers(@Path("id") String id);

    /**
     * Returns a list of {@link Track} objects that were submitted to a group with a given ID, but have not yet been
     * approved.
     *
     * @param id ID of the group to get pending tracks for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/pending_tracks")
    Call<List<Track>> getGroupPendingTracks(
            @Path("id") String id);

    /**
     * Returns a {@link Track} that was submitted to a group with a given ID, but has not yet been
     * approved.
     *
     * @param id      ID of the group to get a pending track for.
     * @param trackId ID of the pending track that was submitted to a group.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/pending_tracks/{pending-id}")
    Call<Track> getGroupPendingTrack(
            @Path("id") String id, @Path("pending-id") String trackId);

    /**
     * Returns a list of {@link Track} objects that were contributed to a group with a given ID. For moderators.
     *
     * @param id ID of the group to get pending tracks for.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/contributions")
    Call<List<Track>> getGroupContributions(@Path("id") String id);

    /**
     * Returns a {@link Track} that was contributed to a group with a given ID. For moderators.
     *
     * @param id      ID of the group to get a contribution for.
     * @param trackId ID of the contribution.
     * @return The call that can be used to get the data.
     */
    @GET("groups/{id}/pending_tracks/{contribution-id}")
    Call<Track> getGroupContribution(@Path("id") String id, @Path("contribution-id") String trackId);

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
     * @return The call that can be used to get the data.
     */
    @GET("me")
    Call<User> getMe();

    /**
     * Returns a list of {@link Track} items for the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/tracks")
    Call<List<Track>> getMyTracks();

    /**
     * Returns a list of {@link Playlist} items for the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/playlists")
    Call<List<Playlist>> getMyPlaylists();

    /**
     * Returns {@link Users} followed by the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/followings")
    Call<List<User>> getMyFollowings();

    /**
     * Returns a {@link User} followed by the authenticated user.
     *
     * @param followedUserId ID of the followed user.
     * @return The call that can be used to get the data.
     */
    @GET("me/followings/{following-id}")
    Call<User> getMyFollowing(@Path("following-id") String followedUserId);

    /**
     * Returns {@link Users} followed by the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/followers")
    Call<List<User>> getMyFollowers();

    /**
     * Returns a {@link User} followed by the authenticated user.
     *
     * @param followerId ID of the follower.
     * @return The call that can be used to get the data.
     */
    @GET("me/followers/{follower-id}")
    Call<User> getMyFollower(
            @Path("follower-id") String followerId);

    /**
     * Returns {@link Comments} for the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/comments")
    Call<List<Comment>> getMyComments();

    /**
     * Returns a list of favorited {@link Track} items for the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/favorites")
    Call<List<Track>> getMyFavorites();

    /**
     * Returns a favorited {@link Track} for the authenticated user.
     *
     * @param favoriteId ID of the track in the user's favorites.
     * @return The call that can be used to get the data.
     */
    @GET("me/favorites/{favorite-id}")
    Call<List<Track>> getMyFavorite(
            @Path("favorite-id") String favoriteId);

    /**
     * Returns a list of groups that the authenticated user is a part of.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/groups")
    Call<List<Group>> getMyGroups();

    /**
     * Returns a list of web profiles that the authenticated user has.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/web-profiles")
    Call<List<WebProfile>> getMyWebProfiles();

    /**
     * Returns {@link Connections} for the authenticated user.
     *
     * @return The call that can be used to get the data.
     */
    @GET("me/connections")
    Call<List<Connection>> getMyConnections();

    /**
     * Returns a {@link Connection} for the authenticated user.
     *
     * @param connectionId ID of the connection.
     * @return The call that can be used to get the data.
     */
    @GET("me/connections")
    Call<Connection> getMyConnection(String connectionId);
}
