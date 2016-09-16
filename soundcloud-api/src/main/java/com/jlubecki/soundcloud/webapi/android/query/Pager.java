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

import java.util.HashMap;

/**
 * Wraps a {@link Query} object to create a simple paging object.
 */
public class Pager {

    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    public static final int LIMIT_DEFAULT = 50;
    public static final int LIMIT_MAX = 200;

    private HashMap<String, String> queryMap;
    private int limit = LIMIT_DEFAULT;
    private int offset = 0;

    public Pager(Query query) {
        this.queryMap = query.createMap();

        updateLimit(limit);
        updateOffset(offset);
    }

    public Pager(Query query, @IntRange(from = 1, to = 200) int pageSize) {
        this.queryMap = query.createMap();

        this.limit = pageSize;

        updateLimit(limit);
        updateOffset(offset);
    }

    /**
     * Updates the query offset by subtracting the page size from the current offset.
     *
     * @return The updated query map to get the previous result set.
     */
    public HashMap<String, String> previous() {
        updateOffset(offset - limit);

        return queryMap;
    }

    public HashMap<String, String> next() {
        updateOffset(offset + limit);

        return queryMap;
    }

    public void setPageSize(int pageSize) {
        updateLimit(pageSize);
    }

    public void setOffset(int offset) {
        updateOffset(offset);

        if (offset < 0) {
            returnToStart();
        }
    }

    public void reset() {
        updateOffset(0);
        updateLimit(LIMIT_DEFAULT);
    }

    public void returnToStart() {
        updateOffset(0);
    }

    private void updateLimit(@IntRange(from = 1, to = 200) int limit) {
        this.limit = limit;

        queryMap.put(LIMIT, String.valueOf(limit));
    }

    private void updateOffset(int offset) {
        this.offset = offset;

        queryMap.put(OFFSET, String.valueOf(offset));
    }
}
