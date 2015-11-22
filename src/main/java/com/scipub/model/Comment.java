package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A comment at either a publication or a peer review
 * 
 * @author bozhanov
 */
@Entity
@Table(name = "comments")
public class Comment extends BaseTimedEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    private PublicationRevision publication;

    @ManyToOne
    private PeerReviewRevision peerReview;

    @Lob
    private String content;

    @Column(nullable = false)
    private boolean inline;
    
    @Column(nullable = false)
    private int inlineSelectionStart;
    
    @Column(nullable = false)
    private int inlineSelectionLength;
    
    @Column(nullable = false)
    private boolean addressed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PublicationRevision getPublication() {
        return publication;
    }

    public void setPublication(PublicationRevision publication) {
        this.publication = publication;
    }

    public PeerReviewRevision getPeerReview() {
        return peerReview;
    }

    public void setPeerReview(PeerReviewRevision peerReview) {
        this.peerReview = peerReview;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public int getInlineSelectionStart() {
        return inlineSelectionStart;
    }

    public void setInlineSelectionStart(int inlineSelectionStart) {
        this.inlineSelectionStart = inlineSelectionStart;
    }

    public int getInlineSelectionLength() {
        return inlineSelectionLength;
    }

    public void setInlineSelectionLength(int inlineSelectionLength) {
        this.inlineSelectionLength = inlineSelectionLength;
    }

    public boolean isAddressed() {
        return addressed;
    }

    public void setAddressed(boolean addressed) {
        this.addressed = addressed;
    }
}
