package com.scipub.dao.jpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.scipub.model.PeerReview;
import com.scipub.model.Publication;
import com.scipub.model.PublicationSource;
import com.scipub.model.User;

@Repository
public class PeerReviewDao extends Dao {

    public List<PeerReview> getPeerReviews(User user) {
        //TODO
        return null;
    }
}
