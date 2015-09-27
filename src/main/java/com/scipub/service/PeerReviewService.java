package com.scipub.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dto.PeerReviewDto;
import com.scipub.model.PeerReview;
import com.scipub.model.Publication;
import com.scipub.model.PublicationPreliminaryReview;
import com.scipub.model.User;
import com.scipub.util.UriUtils;

@Service
public class PeerReviewService {

    @Inject
    private PeerReviewDao dao;
    
    @Transactional
    public String submitPeerReview(PeerReviewDto dto, String reviewerId) {
        PeerReview review = dtoToEntity(dto, reviewerId);
        review = dao.persist(review);
        return review.getUri();
    }

    
    @Transactional
    public void submitPreliminaryReview(String reviewerId, String publicationUri, boolean acceptable) {
        User reviewer = dao.getById(User.class, reviewerId);
        Publication publication = dao.getById(Publication.class, publicationUri);
        
        PublicationPreliminaryReview preliminaryReview = new PublicationPreliminaryReview();
        preliminaryReview.setAcceptable(acceptable);
        preliminaryReview.setReviewer(reviewer);
        preliminaryReview.setPublication(publication);
        preliminaryReview.setCreated(LocalDateTime.now());
    }
    
    private PeerReview dtoToEntity(PeerReviewDto dto, String reviewerId) {
        User reviewer = dao.getById(User.class, reviewerId);
        Publication publication = dao.getById(Publication.class, dto.getPublicationUri());
        
        PeerReview review = new PeerReview();
        review.setUri(UriUtils.generateReviewUri());        
        review.setPublication(publication);
        review.setReviewer(reviewer);
        
        review.setClarityOfBackground(dto.getClarityOfBackground());
        review.setDataAnalysis(dto.getDataAnalysis());
        review.setNoveltyOfConclusions(dto.getNoveltyOfConclusions());
        review.setImportance(dto.getImportance());
        review.setQualityOfPresentation(dto.getQualityOfPresentation());
        review.setStudyDesignAndMethods(dto.getStudyDesignAndMethods());
        
        
        review.setCreated(LocalDateTime.now());
        
        return review;
    }
}
