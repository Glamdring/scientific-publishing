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
}
