package com.scipub.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "peer_reviews")
@NamedQueries(
        @NamedQuery(
                name = "PeerReview.getByReviewerAndPublication",
                query = "SELECT pr FROM PeerReview pr WHERE pr.reviewer=:reviewer AND pr.publication=:publication"))
public class PeerReview extends BaseTimedEntity {

    @Id
    private String uri;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User reviewer;

    @Column(nullable = false)
    private int clarityOfBackground;

    @Column(nullable = false)
    private int importance;

    @Column(nullable = false)
    private int studyDesignAndMethods;

    @Column(nullable = false)
    private int dataAnalysis;

    @Column(nullable = false)
    private int noveltyOfConclusions;

    @Column(nullable = false)
    private int qualityOfPresentation;

    @ManyToOne(cascade = CascadeType.ALL)
    private PeerReviewRevision currentRevision;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public int getClarityOfBackground() {
        return clarityOfBackground;
    }

    public void setClarityOfBackground(int clarityOfBackground) {
        this.clarityOfBackground = clarityOfBackground;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getStudyDesignAndMethods() {
        return studyDesignAndMethods;
    }

    public void setStudyDesignAndMethods(int studyDesignAndMethods) {
        this.studyDesignAndMethods = studyDesignAndMethods;
    }

    public int getDataAnalysis() {
        return dataAnalysis;
    }

    public void setDataAnalysis(int dataAnalysis) {
        this.dataAnalysis = dataAnalysis;
    }

    public int getNoveltyOfConclusions() {
        return noveltyOfConclusions;
    }

    public void setNoveltyOfConclusions(int noveltyOfConclusions) {
        this.noveltyOfConclusions = noveltyOfConclusions;
    }

    public int getQualityOfPresentation() {
        return qualityOfPresentation;
    }

    public void setQualityOfPresentation(int qualityOfPresentation) {
        this.qualityOfPresentation = qualityOfPresentation;
    }

    public PeerReviewRevision getCurrentRevision() {
        return currentRevision;
    }

    public void setCurrentRevision(PeerReviewRevision currentRevision) {
        this.currentRevision = currentRevision;
    }
}
