/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "peer_reviews")
@NamedQueries({
        @NamedQuery(
                name = "PeerReview.getByReviewerAndPublication",
                query = "SELECT pr FROM PeerReview pr WHERE pr.reviewer=:reviewer AND pr.publication=:publication"),
        @NamedQuery(
                name = "PeerReview.getByPublication",
                query = "SELECT pr FROM PeerReview pr WHERE pr.publication=:publication ORDER BY pr.created DESC")})
@Audited
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
    private int significance;

    @Column(nullable = false)
    private int studyDesignAndMethods;

    @Column(nullable = false)
    private int dataAnalysis;

    @Column(nullable = false)
    private int noveltyOfConclusions;

    @Column(nullable = false)
    private int qualityOfPresentation;

    @ManyToOne(cascade = CascadeType.ALL)
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    private PeerReviewRevision currentRevision;

    @Column(nullable = false)
    private boolean conflictOfInterestsDeclaration;
    
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

    public int getSignificance() {
        return significance;
    }

    public void setSignificance(int significance) {
        this.significance = significance;
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

    public boolean isConflictOfInterestsDeclaration() {
        return conflictOfInterestsDeclaration;
    }

    public void setConflictOfInterestsDeclaration(boolean conflictOfInterestsDeclaration) {
        this.conflictOfInterestsDeclaration = conflictOfInterestsDeclaration;
    }
}
