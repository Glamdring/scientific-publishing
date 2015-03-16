package com.scipub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class PublicationEndorsement {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User voter;

    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication paper) {
        this.publication = paper;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }
}
