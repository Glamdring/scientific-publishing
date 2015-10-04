package com.scipub.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dto.PeerReviewDto;
import com.scipub.model.PeerReview;
import com.scipub.model.PeerReviewRevision;
import com.scipub.model.Publication;
import com.scipub.model.PublicationPreliminaryReview;
import com.scipub.model.User;
import com.scipub.util.UriUtils;

@Service
public class PeerReviewService {

    @Inject
    private PeerReviewDao dao;
    
    @Transactional
    public String submitPeerReview(PeerReviewDto dto, UUID reviewerId) {
        User reviewer = getUser(reviewerId);
        Publication publication = getPublication(dto.getPublicationUri());
        
        PeerReview review = dao.getPeerReview(reviewer, publication).orElse(new PeerReview());
        
        fillEntity(dto, review, reviewer, publication);
     
        PeerReviewRevision revision = new PeerReviewRevision();
        revision.setPeerReview(review);
        revision.setContent(dto.getContent());

        review.setCurrentRevision(revision);
        
        review = dao.persist(review);
        
        return review.getUri();
    }

    
    @Transactional
    public void submitPreliminaryReview(UUID reviewerId, String publicationUri, boolean acceptable) {
        User reviewer = getUser(reviewerId);
        Publication publication = getPublication(publicationUri);
        
        // cleanup any previous vote
        dao.getPreliminaryReview(reviewer, publication).ifPresent(pr -> dao.delete(pr));
        
        PublicationPreliminaryReview preliminaryReview = new PublicationPreliminaryReview();
        preliminaryReview.setAcceptable(acceptable);
        preliminaryReview.setReviewer(reviewer);
        preliminaryReview.setPublication(publication);
        preliminaryReview.setCreated(LocalDateTime.now());
        
        dao.persist(preliminaryReview);
    }
    
    @Transactional(readOnly = true)
    public Optional<Boolean> getPreliminaryReview(UUID userId, String publicationUri) {
        User user = getUser(userId);
        Publication publication = getPublication(publicationUri);
        
        Optional<PublicationPreliminaryReview> review = dao.getPreliminaryReview(user, publication);
        return review.map(r -> r.isAcceptable());
    }
    
    @Transactional(readOnly = true)
    public Optional<PeerReviewDto> getPeerReview(UUID userId, String publicationUri) {
        User user = getUser(userId);
        Publication publication = getPublication(publicationUri);
        
        Optional<PeerReview> review = dao.getPeerReview(user, publication);
        return review.map(r -> entityToDto(r));
    }


    private Publication getPublication(String publicationUri) {
        Preconditions.checkNotNull(publicationUri, "publicationUri can't be null");
        Publication publication = dao.getById(Publication.class, publicationUri);
        Preconditions.checkNotNull(publication, "Publication not found");
        return publication;
    }


    private User getUser(UUID userId) {
        Preconditions.checkNotNull(userId, "userId can't be null");
        User user = dao.getById(User.class, userId);
        Preconditions.checkNotNull(user, "User not found");
        return user;
    }
    
    private PeerReviewDto entityToDto(PeerReview peerReview) {
        Preconditions.checkNotNull(peerReview);
        PeerReviewDto dto = new PeerReviewDto();
        dto.setClarityOfBackground(peerReview.getClarityOfBackground());
        dto.setDataAnalysis(peerReview.getDataAnalysis());
        dto.setImportance(peerReview.getImportance());
        dto.setNoveltyOfConclusions(peerReview.getNoveltyOfConclusions());
        dto.setQualityOfPresentation(peerReview.getQualityOfPresentation());
        dto.setStudyDesignAndMethods(peerReview.getStudyDesignAndMethods());
        
        dto.setPublicationUri(peerReview.getPublication().getUri());
        dto.setUri(peerReview.getUri());
        
        return dto;
    }


    private void fillEntity(PeerReviewDto dto, PeerReview review, User reviewer, Publication publication) {
        Preconditions.checkNotNull(dto);
        Preconditions.checkNotNull(review);
        
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
    }
}
