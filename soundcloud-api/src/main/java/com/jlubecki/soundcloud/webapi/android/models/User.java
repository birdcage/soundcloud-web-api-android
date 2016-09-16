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

package com.jlubecki.soundcloud.webapi.android.models;

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

    @SerializedName("discogs-name")
    public String discogs_name;

    @SerializedName("myspace-name")
    public String myspace_name;

    public String website;

    @SerializedName("website-tile")
    public String website_title;

    @SerializedName("online")
    public boolean is_online;

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
