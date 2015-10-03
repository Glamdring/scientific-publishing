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

    public List<PeerReview> getPeerReviews(User user) {
        //TODO
        return null;
    }
    
    public Optional<PeerReview> getPeerReview(User user, Publication publication) {
        QueryDetails<PeerReview> details = new QueryDetails<>();
        details.setQueryName("PeerReview.getByReviewerAndPublication");
        details.setParamNames(new String[]{"reviewer", "publication"});
        details.setParamValues(new Object[] {user, publication});
        details.setCount(1);
        return Optional.ofNullable(getSingleResult(findByQuery(details)));
    }

    public Optional<PublicationPreliminaryReview> getPreliminaryReview(User user, Publication publication) {
        QueryDetails<PublicationPreliminaryReview> details = new QueryDetails<>();
        details.setQueryName("PublicationPreliminaryReview.getByReviewerAndPublication");
        details.setParamNames(new String[]{"reviewer", "publication"});
        details.setParamValues(new Object[] {user, publication});
        details.setCount(1);
        return Optional.ofNullable(getSingleResult(findByQuery(details)));
    }
}
