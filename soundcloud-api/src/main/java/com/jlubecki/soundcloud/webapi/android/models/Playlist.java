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

import java.util.List;

/**
 * Representation of a SoundCloud Playlist.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#playlists">
 * SoundCloud Playlist Reference</a>
 */
public class Playlist {

    public String kind;

    public String id;

    public String created_at;

    public String user_id;

    public String duration;

    public String sharing;

    /**
     * The tag_list property contains a list of tags separated by spaces.
     * Multiword tags are quoted in doublequotes.
     * Machine tags that follow the pattern NAMESPACE:KEY=VALUE are supported.
     * For example:
     * <p/>
     * <ul>
     * <li>geo:lat=43.555</li>
     * <li>camel:size=medium</li>
     * <li>“machine:tag=with space”</li>
     * </ul>
     * <p/>
     * Machine tags are not revealed to the user on the track pages.
     */
    public String tag_list;

    public String permalink;

    public String track_count;

    /**
     * This will aggregate the playlists tracks streamable attribute.
     * Its value will be nil if not all tracks have the same streamable value.
     */
    @SerializedName("streamable")
    public boolean is_streamable;

    /**
     * This will aggregate the playlists tracks downloadable attribute.
     * Its value will be nil if not all tracks have the same downloadable value.
     */
    @SerializedName("downloadable")
    public boolean is_downloadable;

    public String embeddable_by;

    public String purchase_url;

    public String label_id;

    /**
     * Possible values:
     * <ul>
     * <li>{@link Type#EP_SINGLE}</li>
     * <li>{@link Type#ALBUM}</li>
     * <li>{@link Type#COMPILATION}</li>
     * <li>{@link Type#PROJECT_FILES}</li>
     * <li>{@link Type#ARCHIVE}</li>
     * <li>{@link Type#SHOWCASE}</li>
     * <li>{@link Type#DEMO}</li>
     * <li>{@link Type#SAMPLE_PACK}</li>
     * <li>{@link Type#OTHER}</li>
     * </ul>
     *
     * @see Type
     */
    public String type;

    public String playlist_type;

    /**
     * EAN identifier for the playlist.
     */
    public String ean;

    public String description;

    public String genre;

    public String release;

    public String purchase_title;

    public String label_name;

    public String title;

    public String release_year;

    public String release_month;

    public String release_day;

    public String license;

    public String uri;

    public String permalink_url;

    /**
     * JPEG, PNG and GIF are accepted when uploading and will be encoded to multiple JPEGs in these
     * formats:
     * <p/>
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
     * <p/>
     * The URL is pointing to the format large by default.
     * If you want to use a different format you have to replace large
     * with the specific format name in the image URL.
     *
     * @see <a href="https://developers.soundcloud.com/docs/api/reference#artwork_url">Album Artwork
     * Reference</a>
     */
    public String artwork_url;

    public MiniUser user;

    public List<Track> tracks;

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     *                                    ~~ Possible Values ~~
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Describes the type of playlist.
     *
     * @see <a href="https://developers.soundcloud.com/docs/api/reference#playlist_type">Playlist Type
     * Reference</a>
     */
    public static class Type {
        public static final String EP_SINGLE = "ep single";
        public static final String ALBUM = "album";
        public static final String COMPILATION = "compilation";
        public static final String PROJECT_FILES = "project files";
        public static final String ARCHIVE = "archive";
        public static final String SHOWCASE = "showcase";
        public static final String DEMO = "demo";
        public static final String SAMPLE_PACK = "sample pack";
        public static final String OTHER = "other";
    }

    /**
     * Describes who has permission to embed a playlist.
     */
    public static class EmbeddableBy {
        public static final String ALL = "all";
        public static final String ME = "me";
        public static final String NONE = "none";
    }
}
