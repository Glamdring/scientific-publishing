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
