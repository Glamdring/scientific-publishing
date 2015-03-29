package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class PeerReview {

    @Id
    private String uri;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User reviewer;

    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime created;

    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime modified;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String doi) {
        this.uri = doi;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication paper) {
        this.publication = paper;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
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
}
