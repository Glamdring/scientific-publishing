package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="publication_preliminary_reviews")
@NamedQueries(
        @NamedQuery(
                name = "PublicationPreliminaryReview.getByReviewerAndPublication",
                query = "SELECT pr FROM PublicationPreliminaryReview pr WHERE pr.reviewer=:reviewer AND pr.publication=:publication"))
public class PublicationPreliminaryReview extends BaseTimedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private boolean acceptable;

    @ManyToOne
    private User reviewer;
    
    @ManyToOne
    private Publication publication;
    
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

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }
}
