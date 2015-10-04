package com.scipub.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dao.jpa.PublicationDao;
import com.scipub.dao.jpa.UserDao;
import com.scipub.model.User;

@Service
public class ForgetUserService {

    @Inject
    private PublicationDao publicationDao;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private PeerReviewDao peerReviewDao;
    
    @Transactional
    public void foretUser(UUID userId) {
        Preconditions.checkNotNull(userId);
        User user = userDao.getById(User.class, userId);
        publicationDao.deletePublications(user);
        peerReviewDao.deleteAllReviews(user);
        //TODO comments, inline comments, resources,
        userDao.forgetUser(user);
    }
}
