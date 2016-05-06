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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scipub.web.UserContext;
import com.scipub.web.util.RequireUserLoggedIn;

@Component
public class RequireLoggedInUserInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequireLoggedInUserInterceptor.class);
    
    @Inject
    private UserContext userContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) { // potentially slow, should revisit
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            RequireUserLoggedIn requiresLoggedIn = handlerMethod.getMethodAnnotation(RequireUserLoggedIn.class);
    
            if (requiresLoggedIn != null && userContext.getUser() == null) {
                logger.info("Attempting to access a page that requires a logged user, but no user is logged in");
                response.sendRedirect(request.getServletContext().getContextPath() + "/signin");
                return false;
            }
        }
        return true;
    }
}
