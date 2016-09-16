/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jacob Lubecki
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

package com.jlubecki.soundcloud.webapi.android.auth.models;

/**
 * Representation of the API response from the authentication endpoint.
 *
 * @see <a href="https://developers.soundcloud.com/docs/api/reference#token">Auth Reference</a>
 */
public class AuthenticationResponse {

    public String access_token;

    public String scope;

    public String error;

    /**
     * Helper method to determine authentication response type.
     *
     * @return The type of the authentication response.
     */
    public ResponseType getType() {
        if (access_token != null) {
            return ResponseType.TOKEN;
        }

        if (error != null) {
            return ResponseType.ERROR;
        }

        return ResponseType.UNKNOWN;
    }

    public enum ResponseType {
        TOKEN,
        ERROR,
        UNKNOWN
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "access_token='" + access_token + '\'' +
                ", scope='" + scope + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
