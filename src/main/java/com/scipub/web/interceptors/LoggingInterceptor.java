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

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.info("Started '{}', url '{}' and params '{}'", method.getMethod().getName(),
                    request.getRequestURI(), getParamString(request.getParameterMap()));
        } else if (!(handler instanceof ResourceHttpRequestHandler)) {
            logger.warn("Handler is unexpectedly not a HandlerMethod: " + handler);
        }
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.info("Finished '{}' url '{}' and params '{}'", method.getMethod().getName(),
                    request.getRequestURI(), getParamString(request.getParameterMap()));
        } else if (!(handler instanceof ResourceHttpRequestHandler)) {
            logger.warn("Handler is unexpectedly not a HandlerMethod: " + handler);
        }   
    }
    
    private Object getParamString(Map<String, String[]> parameterMap) {
        StringBuilder sb = parameterMap.entrySet().stream().collect(StringBuilder::new, 
                (s, e) -> s.append(e.getKey() + "=" + Arrays.toString(e.getValue())), 
                (s1, s2) -> s1.append(s2.toString()));
        return sb.toString();
    }

}
