package com.scipub.dto;

import java.util.Set;

import com.scipub.model.PaperStatus;

public class PaperSubmissionDto {

    private String uri; 
    private Set<Integer> branchIds;
    private Set<String> authorIds;
    private Long submitterId;
    private Set<String> nonRegisteredAuthors;
    private PaperStatus status;
    private Set<String> tags;
    private String followUpTo;
    private String followUpToLink;
    private String followUpToDoi;
    private String title;
    private String paperAbstract;
    private String content;
    private String originalFilename;
    private byte[] originalFileContent;
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public Set<Integer> getBranchIds() {
        return branchIds;
    }
    public void setBranchIds(Set<Integer> branchIds) {
        this.branchIds = branchIds;
    }
    public Set<String> getAuthorIds() {
        return authorIds;
    }
    public void setAuthorIds(Set<String> authorIds) {
        this.authorIds = authorIds;
    }
    public Long getSubmitterId() {
        return submitterId;
    }
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }
    public Set<String> getNonRegisteredAuthors() {
        return nonRegisteredAuthors;
    }
    public void setNonRegisteredAuthors(Set<String> nonRegisteredAuthors) {
        this.nonRegisteredAuthors = nonRegisteredAuthors;
    }
    public PaperStatus getStatus() {
        return status;
    }
    public void setStatus(PaperStatus status) {
        this.status = status;
    }
    public Set<String> getTags() {
        return tags;
    }
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    public String getFollowUpTo() {
        return followUpTo;
    }
    public void setFollowUpTo(String followUpTo) {
        this.followUpTo = followUpTo;
    }
    public String getFollowUpToLink() {
        return followUpToLink;
    }
    public String getFollowUpToDoi() {
        return followUpToDoi;
    }
    public void setFollowUpToDoi(String followUpToDoi) {
        this.followUpToDoi = followUpToDoi;
    }
    public void setFollowUpToLink(String followUpToLink) {
        this.followUpToLink = followUpToLink;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPaperAbstract() {
        return paperAbstract;
    }
    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getOriginalFilename() {
        return originalFilename;
    }
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
    public byte[] getOriginalFileContent() {
        return originalFileContent;
    }
    public void setOriginalFileContent(byte[] originalFileContent) {
        this.originalFileContent = originalFileContent;
    }
}
