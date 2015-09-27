package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="peer_review_revisions")
public class PeerReviewRevision {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private PeerReview peerReview;

    @Column
    private String content;
    
    //TODO time?

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PeerReview getPeerReview() {
        return peerReview;
    }

    public void setPeerReview(PeerReview peerReview) {
        this.peerReview = peerReview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
