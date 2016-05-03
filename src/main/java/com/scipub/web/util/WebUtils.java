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
package com.scipub.web.util;

import javax.servlet.http.Cookie;

import org.joda.time.DateTimeConstants;

import com.scipub.model.User;

public class WebUtils {
    public static final int COOKIE_AGE = DateTimeConstants.SECONDS_PER_WEEK;
    public static final String ROOT_DOMAIN = ".scienation.com";
    
    public static Cookie createCookie(User user, String cookieName, String cookieValue) {
        Cookie authTokenCookie = new Cookie(cookieName, cookieValue);
        authTokenCookie.setMaxAge(COOKIE_AGE);
        authTokenCookie.setPath("/");
        authTokenCookie.setDomain(ROOT_DOMAIN);
        return authTokenCookie;
    }
}
