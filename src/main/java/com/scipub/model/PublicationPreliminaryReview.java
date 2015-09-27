package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="publication_preliminary_reviews")
public class PublicationPreliminaryReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private boolean acceptable;

    @ManyToOne
    private User reviewer;
    
    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public void setAcceptable(boolean acceptable) {
        this.acceptable = acceptable;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }
}
