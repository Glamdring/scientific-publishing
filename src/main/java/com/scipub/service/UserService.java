package com.scipub.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.Dao;
import com.scipub.model.Paper;
import com.scipub.model.User;

@Service
public class UserService {

    @Inject
    private Dao dao;
    
    @Transactional(readOnly = true)
    public boolean isAuthor(User user, String paperUri) {
        Paper paper = dao.getById(Paper.class, paperUri);
        return paper.getAuthors().contains(user);
    }

}
