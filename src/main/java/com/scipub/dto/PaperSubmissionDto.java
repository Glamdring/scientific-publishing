package com.scipub.dto;

import java.util.List;

import com.scipub.model.PaperStatus;

public class PaperSubmissionDto {

    private String doi; 
    private List<Integer> branchIds;
    private List<Long> authorIds;
    private Long submitterId;
    private List<String> nonRegisteredAuthors;
    private PaperStatus status;
    private List<String> tags;
    private String followUpTo;
    private String followUpToLink;
    private String title;
    private String paperAbstract;
    private String content;
    private String originalFilename;
    public String getDoi() {
        return doi;
    }
    public void setDoi(String doi) {
        this.doi = doi;
    }
    public List<Integer> getBranchIds() {
        return branchIds;
    }
    public void setBranchIds(List<Integer> branchIds) {
        this.branchIds = branchIds;
    }
    public List<Long> getAuthorIds() {
        return authorIds;
    }
    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }
    public Long getSubmitterId() {
        return submitterId;
    }
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }
    public List<String> getNonRegisteredAuthors() {
        return nonRegisteredAuthors;
    }
    public void setNonRegisteredAuthors(List<String> nonRegisteredAuthors) {
        this.nonRegisteredAuthors = nonRegisteredAuthors;
    }
    public PaperStatus getStatus() {
        return status;
    }
    public void setStatus(PaperStatus status) {
        this.status = status;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
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
}
