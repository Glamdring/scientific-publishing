/*
 * Computoser is a music-composition algorithm and a website to present the results
 * Copyright (C) 2012-2014  Bozhidar Bozhanov
 *
 * Computoser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Computoser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Computoser.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.scipub.dao.jpa;

public class QueryDetails<T> {

    private String query;
    private String queryName;
    private String[] paramNames = new String[0];
    private Object[] paramValues = new Object[0];
    private int start = -1;
    private int count = -1;
    private boolean cacheable;
    private Class<T> resultClass;

    public String getQuery() {
        return query;
    }
    public QueryDetails<T> setQuery(String query) {
        this.query = query;
        return this;
    }
    public String[] getParamNames() {
        return paramNames;
    }
    public QueryDetails<T> setParamNames(String[] paramNames) {
        this.paramNames = paramNames.clone();
        return this;
    }
    public Object[] getParamValues() {
        return paramValues;
    }
    public QueryDetails<T> setParamValues(Object[] paramValues) {
        this.paramValues = paramValues.clone();
        return this;
    }
    public int getStart() {
        return start;
    }
    public QueryDetails<T> setStart(int start) {
        this.start = start;
        return this;
    }
    public int getCount() {
        return count;
    }
    public QueryDetails<T> setCount(int count) {
        this.count = count;
        return this;
    }
    public boolean isCacheable() {
        return cacheable;
    }
    public QueryDetails<T> setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }
    public String getQueryName() {
        return queryName;
    }
    public QueryDetails<T> setQueryName(String queryName) {
        this.queryName = queryName;
        return this;
    }
    public Class<T> getResultClass() {
        return resultClass;
    }
    public QueryDetails<T> setResultClass(Class<T> resultClass) {
        this.resultClass = resultClass;
        return this;
    }
}
