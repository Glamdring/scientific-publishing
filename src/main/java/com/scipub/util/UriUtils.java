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
package com.scipub.util;

import java.util.UUID;

/**
 * Utility used to generate URIs for papers. Based on UUID. Also supports DOI for external resources
 * 
 * @author bozhanov
 */
public class UriUtils {

    private static final String PREFIX = "scipub:";
    private static final String REVIEW_PREFIX = "scipub:review:";
    private static final String DOI_PREFIX = "http://dx.doi.org/";
    
    public static String generateUri() {
        return PREFIX + UUID.randomUUID().toString();
    }
    
    public static String generateReviewUri() {
        return REVIEW_PREFIX + UUID.randomUUID().toString();
    }
    
    public static String getDoiUri(String doi) {
        return DOI_PREFIX + doi;
    }
}
