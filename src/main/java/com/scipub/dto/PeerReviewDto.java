package com.scipub.dto;


public class PeerReviewDto {

    private String uri;

    private String publicationUri;

    private int clarityOfBackground;
    
    private int importance;
    
    private int studyDesignAndMethods;
    
    private int dataAnalysis;
    
    private int noveltyOfConclusions;
    
    private int qualityOfPresentation;

    private String content;
    
    private boolean conflictOfInterestsDeclaration;
    
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clarityOfBackground;
        result = prime * result + (conflictOfInterestsDeclaration ? 1231 : 1237);
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + dataAnalysis;
        result = prime * result + importance;
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
        if (importance != other.importance)
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
                + clarityOfBackground + ", importance=" + importance + ", studyDesignAndMethods="
                + studyDesignAndMethods + ", dataAnalysis=" + dataAnalysis + ", noveltyOfConclusions="
                + noveltyOfConclusions + ", qualityOfPresentation=" + qualityOfPresentation + ", content=" + content
                + ", conflictOfInterestsDeclaration=" + conflictOfInterestsDeclaration + "]";
    }
}
