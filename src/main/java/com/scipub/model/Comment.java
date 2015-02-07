package com.scipub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Comment {

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;
    
    @ManyToOne
    private Paper paper;
    
    @ManyToOne
    private PeerReview peerReview;
    
    @Type(type="com.scipub.util.PersistentDateTime")
    private DateTime created;
    
    @Lob
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public PeerReview getPeerReview() {
        return peerReview;
    }

    public void setPeerReview(PeerReview peerReview) {
        this.peerReview = peerReview;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
