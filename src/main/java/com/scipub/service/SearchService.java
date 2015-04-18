package com.scipub.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public <T> List<T> search(String keywords, Class<T> resultType, SearchType searchType) {
        return Collections.emptyList();
    }
    
    public static enum SearchType {
        START, FULL
    }
}
