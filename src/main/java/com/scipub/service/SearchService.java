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
package com.scipub.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public <T> List<T> search(String keywords, Class<T> resultType, SearchType searchType, String... fields) {
        return Collections.emptyList();
    }
    
    public static enum SearchType {
        START, FULL
    }
}
