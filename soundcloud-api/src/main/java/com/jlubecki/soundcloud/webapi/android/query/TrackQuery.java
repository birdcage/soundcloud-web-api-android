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

package com.jlubecki.soundcloud.webapi.android.query;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import static com.jlubecki.soundcloud.webapi.android.models.Track.Filter;
import static com.jlubecki.soundcloud.webapi.android.models.Track.License;
import static com.jlubecki.soundcloud.webapi.android.models.Track.Type;

/**
 * Created by Jacob on 9/17/15.
 */
public class TrackQuery extends Query {

    private String query;
    private String tags;
    private Filter filter;
    private License license;
    private int bpmFrom = -1;
    private int bpmTo = -1;
    private int durationFrom = -1;
    private int durationTo = -1;
    private String createdAtFrom;
    private String createdAtTo;
    private String ids;
    private String genres;
    private String types;

    private TrackQuery(Builder builder) {
        query = builder.query;
        tags = builder.tags;
        filter = builder.filter;
        license = builder.license;
        bpmFrom = builder.bpmFrom;
        bpmTo = builder.bpmTo;
        durationFrom = builder.durationFrom;
        durationTo = builder.durationTo;
        createdAtFrom = builder.createdAtFrom;
        createdAtTo = builder.createdAtTo;
        ids = builder.ids;
        genres = builder.genres;
        types = builder.types;
    }

    /**
     * Creates a hashmap that can be used to query the SoundCloud API for tracks.
     *
     * @return The map containing all query parameters specified by the TrackQuery {@link Builder}.
     */
    @Override
    public HashMap<String, String> createMap() {

        HashMap<String, String> queryMap = new HashMap<>();

        if (query != null) {
            queryMap.put("q", query);
        }

        if (tags != null) {
            queryMap.put("tags", tags);
        }

        if (filter != null) {
            queryMap.put("filter", filter.toString());
        }

        if (license != null) {
            queryMap.put("license", license.toString());
        }

        if (bpmFrom != -1) {
            queryMap.put("bpm[from]", String.valueOf(bpmFrom));
        }

        if (bpmTo != -1) {
            queryMap.put("bpm[to]", String.valueOf(bpmTo));
        }

        if (durationFrom != -1) {
            queryMap.put("duration[from]", String.valueOf(durationFrom));
        }

        if (durationTo != -1) {
            queryMap.put("duration[to]", String.valueOf(durationTo));
        }

        if (createdAtFrom != null) {
            queryMap.put("created_at[from]", createdAtFrom);
        }

        if (createdAtTo != null) {
            queryMap.put("created_at[to]", createdAtTo);
        }

        if (ids != null) {
            queryMap.put("ids", ids);
        }

        if (createdAtTo != null) {
            queryMap.put("genres", genres);
        }

        if (createdAtTo != null) {
            queryMap.put("types", types);
        }

        if (queryMap.size() > 0) {
            queryMap.put(Pager.LIMIT, String.valueOf(limit));

            return queryMap;
        } else {
            return null;
        }
    }

    public static class Builder {

        private String query;
        private String tags;
        private Filter filter;
        private License license;
        private int bpmFrom = -1;
        private int bpmTo = -1;
        private int durationFrom = -1;
        private int durationTo = -1;
        private String createdAtFrom;
        private String createdAtTo;
        private String ids;
        private String genres;
        private String types;

        public Builder setQuery(String query) {
            this.query = query;

            return this;
        }

        public Builder setTags(String... tagArray) {
            this.tags = TextUtils.join(", ", tagArray);

            return this;
        }

        public Builder setFilter(Filter filter) {
            this.filter = filter;

            return this;
        }

        public Builder setLicense(License license) {
            this.license = license;

            return this;
        }

        public Builder setTypes(Type... types) {
            this.types = TextUtils.join(", ", types);

            return this;
        }

        public Builder setBpmLimits(@IntRange(from = 0, to = 500) int from,
                                    @IntRange(from = 0, to = 500) int to) {
            this.bpmFrom = from;
            this.bpmTo = to;

            return this;
        }

        /**
         * Sets the acceptable ranges of durations for a track. Durations should be given in
         * milliseconds.
         *
         * @param from The minimum duration of songs to pick.
         * @param to   The maximum duration of songs to pick.
         * @return The instance of the builder that was just updated.
         */
        public Builder setDurationLimits(@IntRange(from = 0, to = 3600) int from,
                                         @IntRange(from = 0, to = 3600) int to) {
            this.durationFrom = from;
            this.durationTo = to;

            return this;
        }

        /**
         * Sets the dates between which results should have been created. Dates should be formatted
         * like this: "yyyy-mm-dd hh:mm:ss"
         *
         * @param from The starting date to pick results from.
         * @param to   The ending date to pick results from.
         * @return The instance of the builder that was just updated.
         */
        public Builder setCreationDateLimits(@Nullable String from, @Nullable String to) {
            this.createdAtFrom = from;
            this.createdAtTo = to;

            return this;
        }

        /**
         * Sets the list of track IDs to search through.
         *
         * @param ids The list of IDs to look through.
         * @return The instance of the builder that was just updated.
         */
        public Builder setIds(String... ids) {
            this.ids = TextUtils.join(", ", ids);

            return this;
        }

        public Builder setGenres(String... genres) {
            this.genres = TextUtils.join(", ", genres);

            return this;
        }

        public TrackQuery build() {
            return new TrackQuery(this);
        }
    }
}
