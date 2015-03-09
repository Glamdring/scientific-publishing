package com.scipub.web;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.scipub.model.User;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserContext implements Serializable {

    private static final long serialVersionUID = 2628029779833903839L;
    
    private User user;

    private boolean loggedInAutomatically;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLoggedInAutomatically() {
        return loggedInAutomatically;
    }

    public void setLoggedInAutomatically(boolean loggedInAutomatically) {
        this.loggedInAutomatically = loggedInAutomatically;
    }
}
