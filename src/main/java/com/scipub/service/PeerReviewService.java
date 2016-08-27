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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dto.PeerReviewDto;
import com.scipub.dto.UserDetails;
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
        
        // we allow only one peer review per reviewer per publication, so a new submission means a new revision
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
        dto.setSignificance(peerReview.getSignificance());
        dto.setNoveltyOfConclusions(peerReview.getNoveltyOfConclusions());
        dto.setQualityOfPresentation(peerReview.getQualityOfPresentation());
        dto.setStudyDesignAndMethods(peerReview.getStudyDesignAndMethods());
        
        dto.setContent(peerReview.getCurrentRevision().getContent());
        dto.setConflictOfInterestsDeclaration(peerReview.isConflictOfInterestsDeclaration());
        dto.setPublicationUri(peerReview.getPublication().getUri());
        dto.setUri(peerReview.getUri());
        
        dto.setReviewer(new UserDetails(peerReview.getReviewer()));
        
        return dto;
    }


    private void fillEntity(PeerReviewDto dto, PeerReview review, User reviewer, Publication publication) {
        Preconditions.checkNotNull(dto);
        Preconditions.checkNotNull(review);
        
        if (review.getUri() == null) {
            review.setUri(UriUtils.generateReviewUri());
        }
        review.setPublication(publication);
        review.setReviewer(reviewer);
        
        review.setClarityOfBackground(dto.getClarityOfBackground());
        review.setDataAnalysis(dto.getDataAnalysis());
        review.setNoveltyOfConclusions(dto.getNoveltyOfConclusions());
        review.setSignificance(dto.getSignificance());
        review.setQualityOfPresentation(dto.getQualityOfPresentation());
        review.setStudyDesignAndMethods(dto.getStudyDesignAndMethods());
        review.setConflictOfInterestsDeclaration(dto.isConflictOfInterestsDeclaration());
        
        review.setCreated(LocalDateTime.now());
    }


    /**
     * Returns a map preliminary reviews
     * 
     * @param publicationUri
     * @return reviewerId -> preliminary acceptability map
     */
    @Transactional(readOnly = true)
    public Map<UUID, Boolean> getPreliminaryReviews(String publicationUri) {
        //TODO pass string instead of a full publication?
        return dao.getPreliminaryReviewsByPublication(getPublication(publicationUri));
    }

    @Transactional(readOnly = true)
    public List<PeerReviewDto> getPeerReviews(String publicationUri) {
        //TODO pass string instead of a full publication?
        return dao.getPeerReviewsByPublication(getPublication(publicationUri))
                .stream().map(pr -> entityToDto(pr))
                .collect(Collectors.toList());
    }
}
