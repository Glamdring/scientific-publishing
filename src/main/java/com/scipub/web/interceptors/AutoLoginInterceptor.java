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
package com.scipub.web.interceptors;

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
import com.scipub.web.SocialSignInAdapter;
import com.scipub.web.UserContext;

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
