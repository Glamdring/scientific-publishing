package com.scipub.web.interceptors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scipub.web.UserContext;
import com.scipub.web.util.UserLoggedIn;

@Component
public class RequireLoggedInUserInterceptor extends HandlerInterceptorAdapter {

    @Inject
    private UserContext userContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        UserLoggedIn requiresLoggedIn = handlerMethod.getMethodAnnotation(UserLoggedIn.class);
 
        if (requiresLoggedIn != null && userContext.getUser() == null) {
            response.sendRedirect("/login");
            return false;
        } else {
            return true;
        }
    }
}
