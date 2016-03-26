/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.service.auth;

public enum AuthenticationProvider {
    PERSONA("persona"),
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    GOOGLE_PLUS("googleplus"),
    LINKED_IN("linkedin");
    
    private final String key;
    
    private AuthenticationProvider(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
