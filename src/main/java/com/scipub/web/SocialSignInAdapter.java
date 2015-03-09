package com.scipub.web;


import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTimeConstants;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import com.scipub.model.User;
import com.scipub.service.UserService;

@Component
public class SocialSignInAdapter implements SignInAdapter {

    private static final int COOKIE_AGE = DateTimeConstants.SECONDS_PER_WEEK;
    public static final String AUTH_TOKEN_COOKIE_NAME = "authToken";
    public static final String AUTH_TOKEN_SERIES_COOKIE_NAME = "authSeries";

    @Inject
    private UserContext context;
    @Inject
    private UserService userService;

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        User user = userService.getUser(userId);
        signIn(user, (HttpServletResponse) request.getNativeResponse(), true);
        HttpSession session = ((HttpServletRequest) request.getNativeRequest()).getSession();
        String redirectUri = (String) session.getAttribute(AuthenticationController.REDIRECT_AFTER_LOGIN);
        if (redirectUri != null) {
            return redirectUri;
        }
        return "/";
    }

    public void signIn(User user, HttpServletResponse response, boolean resetTokens) {
        context.setUser(user);
        if (resetTokens) {
            userService.fillUserWithNewTokens(user, null);
        }
        addPermanentCookies(user, response);
    }

    public void addPermanentCookies(User user, HttpServletResponse response) {
        Cookie authTokenCookie = new Cookie(AUTH_TOKEN_COOKIE_NAME, user.getLoginToken());
        authTokenCookie.setMaxAge(COOKIE_AGE);
        authTokenCookie.setPath("/");
        authTokenCookie.setDomain(".computoser.com");
        response.addCookie(authTokenCookie);

        Cookie seriesCookie = new Cookie(AUTH_TOKEN_SERIES_COOKIE_NAME, user.getLoginSeries());
        seriesCookie.setMaxAge(COOKIE_AGE);
        seriesCookie.setPath("/");
        seriesCookie.setDomain(".computoser.com");
        response.addCookie(seriesCookie);
    }
}
