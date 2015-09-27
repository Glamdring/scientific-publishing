package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * A comment at either a publication or a peer review
 * @author bozhanov
 */
@Entity
@Table(name="comments")
public class Comment extends BaseTimedEntity {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private long id;
    
    @ManyToOne
    private Publication publication;
    
    @ManyToOne
    private PeerReview peerReview;
    
    @Lob
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
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
