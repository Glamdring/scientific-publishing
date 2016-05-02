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
