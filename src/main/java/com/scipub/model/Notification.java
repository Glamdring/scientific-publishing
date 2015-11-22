package com.scipub.model;

import java.util.UUID;

import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

public class Notification extends BaseTimedEntity {

    @Type(type="uuid-char")
    private UUID id;
    
    private NotificationType notificationType;
    
    @ManyToOne
    private Publication publication;
    
    @ManyToOne
    private PeerReview peerReview;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
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
}
