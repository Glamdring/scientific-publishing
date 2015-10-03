package com.scipub.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dao.jpa.PublicationDao;

@Service
public class ForgetUserService {

    @Inject
    private PublicationDao publicationDao;
    
    @Inject
    private PublicationService userDao;
    
    @Inject
    private PeerReviewDao peerReviewDao;
    
    @Transactional
    public void foretUser(String userId) {
        //TODO
    }
}
