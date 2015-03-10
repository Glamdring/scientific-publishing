package com.scipub.web;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.scipub.model.User;
import com.scipub.service.UserService;

@Component
public class AutoLoginInterceptor extends HandlerInterceptorAdapter {

    @Inject
    private UserService userService;

    @Inject
    private UserContext userContext;

    @Inject
    private SocialSignInAdapter adapter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        // don't handle ajax or resource requests
        String requestedWith = request.getHeader("X-Requested-With");
        if (handler instanceof ResourceHttpRequestHandler || (requestedWith != null && requestedWith.equals("XMLHttpRequest"))) {
            return true;
        }

        if (userContext.getUser() == null && request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();

            String authToken = null;
            String series = null;

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(SocialSignInAdapter.AUTH_TOKEN_COOKIE_NAME)) {
                    authToken = cookie.getValue();
                } else if (cookie.getName().equals(SocialSignInAdapter.AUTH_TOKEN_SERIES_COOKIE_NAME)) {
                    series = cookie.getValue();
                }
            }

            if (authToken != null && series != null) {
                User user = userService.rememberMeLogin(authToken, series);
                if (user != null) {
                    adapter.signIn(user, response, false);
                }
                userContext.setLoggedInAutomatically(true);
            }
        }
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        request.setAttribute("userContext", userContext);
    }
}
