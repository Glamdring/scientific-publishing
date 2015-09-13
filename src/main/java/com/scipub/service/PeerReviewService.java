package com.scipub.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dto.PeerReviewDto;
import com.scipub.model.PeerReview;

@Service
public class PeerReviewService {

    @Inject
    private PeerReviewDao dao;
    
    @Transactional
    public String submitPeerReview(PeerReviewDto dto, String userId) {
        PeerReview review = dtoToEntity(dto, userId);
        review = dao.persist(review);
        return review.getUri();
    }

    private PeerReview dtoToEntity(PeerReviewDto dto, String userId) {
        // TODO Auto-generated method stub
        return null;
    }
}
