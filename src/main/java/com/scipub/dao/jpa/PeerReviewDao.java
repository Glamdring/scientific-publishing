package com.scipub.dao.jpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.scipub.model.PeerReview;
import com.scipub.model.PeerReviewRevision;
import com.scipub.model.Publication;
import com.scipub.model.PublicationPreliminaryReview;
import com.scipub.model.User;


@Repository
public class PeerReviewDao extends Dao {

    public List<PeerReview> getPeerReviewsByUser(User user) {
        return getListByPropertyValue(PeerReview.class, "reviewer", user);
    }
    
    public List<PublicationPreliminaryReview> getPreliminaryReviewsByUser(User user) {
        return getListByPropertyValue(PublicationPreliminaryReview.class, "reviewer", user);
    }
    
    public Optional<PeerReview> getPeerReview(User user, Publication publication) {
        QueryDetails<PeerReview> details = new QueryDetails<>();
        details.setQueryName("PeerReview.getByReviewerAndPublication")
               .setParamNames(new String[]{"reviewer", "publication"})
               .setParamValues(new Object[] {user, publication})
               .setCount(1)
               .setResultClass(PeerReview.class);
        
        return Optional.ofNullable(getSingleResult(findByQuery(details)));
    }

    public Optional<PublicationPreliminaryReview> getPreliminaryReview(User user, Publication publication) {
        QueryDetails<PublicationPreliminaryReview> details = new QueryDetails<>();
        details.setQueryName("PublicationPreliminaryReview.getByReviewerAndPublication")
               .setParamNames(new String[]{"reviewer", "publication"})
               .setParamValues(new Object[] {user, publication})
               .setCount(1)
               .setResultClass(PublicationPreliminaryReview.class);
        
        return Optional.ofNullable(getSingleResult(findByQuery(details)));
    }

    public List<PeerReviewRevision> getAllRevisions(PeerReview peerReview) {
        return getListByPropertyValue(PeerReviewRevision.class, "peerReview", peerReview);
    }
    
    public void deleteAllReviews(User user) {
        // first unset the current revision, delete all revisions, and then the peerreview parent itself
        getPeerReviewsByUser(user).forEach(pr -> {
            deletePeerReview(pr);
        });
        
        getPreliminaryReviewsByUser(user).forEach(pr -> delete(pr));
    }

    private void deletePeerReview(PeerReview pr) {
        if (pr.getCurrentRevision() != null) {
            pr.setCurrentRevision(null);
        }
        getAllRevisions(pr).forEach(r -> delete(r));
        delete(pr);
    }

    public Map<UUID, Boolean> getPreliminaryReviewsByPublication(Publication publication) {
        List<PublicationPreliminaryReview> result = getPreliminaryReviewsListByPublication(publication);
        return result.stream().collect(Collectors.toMap(
                ppr -> ppr.getReviewer().getId(),
                ppr -> ppr.isAcceptable()));
    }

    private List<PublicationPreliminaryReview> getPreliminaryReviewsListByPublication(Publication publication) {
        QueryDetails<PublicationPreliminaryReview> details = new QueryDetails<>();
        details.setQueryName("PublicationPreliminaryReview.getByPublication")
               .setParamNames(new String[]{"publication"})
               .setParamValues(new Object[] {publication})
               .setResultClass(PublicationPreliminaryReview.class);
        
        return findByQuery(details);
    }

    public List<PeerReview> getPeerReviewsByPublication(Publication publication) {
        QueryDetails<PeerReview> details = new QueryDetails<>();
        details.setQueryName("PeerReview.getByPublication")
               .setParamNames(new String[]{"publication"})
               .setParamValues(new Object[] {publication})
               .setResultClass(PeerReview.class);
        
        return findByQuery(details);
    }

    public void deletePeerReviewsForPublication(Publication publication) {
        getPeerReviewsByPublication(publication).forEach(pr -> deletePeerReview(pr));
    }

    public void deletePreliminaryReviewsForPublication(Publication publication) {
        getPreliminaryReviewsListByPublication(publication).forEach(pr -> delete(pr));
    }
}
