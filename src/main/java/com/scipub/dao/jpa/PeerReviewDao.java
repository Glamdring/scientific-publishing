package com.scipub.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.scipub.model.PeerReview;
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

    public void deleteAllReviews(User user) {
        getPeerReviewsByUser(user).forEach(pr -> delete(pr));
        getPreliminaryReviewsByUser(user).forEach(pr -> delete(pr));
    }
}
