package com.scipub.util;

import java.util.UUID;

/**
 * Utility used to generate URIs for papers. Based on UUID. Also supports DOI for external resources
 * 
 * @author bozhanov
 */
public class UriUtils {

    private static final String PREFIX = "scipub:";
    private static final String DOI_PREFIX = "http://dx.doi.org/";
    
    public static String generateUri() {
        return PREFIX + UUID.randomUUID().toString();
    }
    
    public static String getDoiUri(String doi) {
        return DOI_PREFIX + doi;
    }
}
