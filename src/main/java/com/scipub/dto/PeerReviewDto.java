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
package com.scipub.dto;


public class PeerReviewDto {

    private String uri;
    private String publicationUri;
    private int clarityOfBackground;
    private int significance;
    private int studyDesignAndMethods;
    private int dataAnalysis;
    private int noveltyOfConclusions;
    private int qualityOfPresentation;
    private String content;
    private boolean conflictOfInterestsDeclaration;
    private UserDetails reviewer;
    
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPublicationUri() {
        return publicationUri;
    }

    public void setPublicationUri(String publicationUri) {
        this.publicationUri = publicationUri;
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

    public void setSignificance(int importance) {
        this.significance = importance;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isConflictOfInterestsDeclaration() {
        return conflictOfInterestsDeclaration;
    }

    public void setConflictOfInterestsDeclaration(boolean conflictOfInterestsDeclaration) {
        this.conflictOfInterestsDeclaration = conflictOfInterestsDeclaration;
    }
    
    public UserDetails getReviewer() {
        return reviewer;
    }

    public void setReviewer(UserDetails reviewer) {
        this.reviewer = reviewer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clarityOfBackground;
        result = prime * result + (conflictOfInterestsDeclaration ? 1231 : 1237);
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + dataAnalysis;
        result = prime * result + significance;
        result = prime * result + noveltyOfConclusions;
        result = prime * result + ((publicationUri == null) ? 0 : publicationUri.hashCode());
        result = prime * result + qualityOfPresentation;
        result = prime * result + studyDesignAndMethods;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PeerReviewDto other = (PeerReviewDto) obj;
        if (clarityOfBackground != other.clarityOfBackground)
            return false;
        if (conflictOfInterestsDeclaration != other.conflictOfInterestsDeclaration)
            return false;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (dataAnalysis != other.dataAnalysis)
            return false;
        if (significance != other.significance)
            return false;
        if (noveltyOfConclusions != other.noveltyOfConclusions)
            return false;
        if (publicationUri == null) {
            if (other.publicationUri != null)
                return false;
        } else if (!publicationUri.equals(other.publicationUri))
            return false;
        if (qualityOfPresentation != other.qualityOfPresentation)
            return false;
        if (studyDesignAndMethods != other.studyDesignAndMethods)
            return false;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PeerReviewDto [uri=" + uri + ", publicationUri=" + publicationUri + ", clarityOfBackground="
                + clarityOfBackground + ", importance=" + significance + ", studyDesignAndMethods="
                + studyDesignAndMethods + ", dataAnalysis=" + dataAnalysis + ", noveltyOfConclusions="
                + noveltyOfConclusions + ", qualityOfPresentation=" + qualityOfPresentation + ", content=" + content
                + ", conflictOfInterestsDeclaration=" + conflictOfInterestsDeclaration + "]";
    }
}
