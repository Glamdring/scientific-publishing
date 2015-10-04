package com.scipub.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.scipub.model.PublicationStatus;

public class PublicationSubmissionDto {

    private String uri; 
    private Set<Long> branchIds = new HashSet<>();
    private Long primaryBranch;
    private Set<UUID> authorIds = new HashSet<>();
    private Set<String> nonRegisteredAuthors = new HashSet<>();
    private PublicationStatus status;
    private Set<String> tags = new HashSet<>();
    private String followUpTo;
    private String followUpToLink;
    private String followUpToDoi;
    private String title;
    private String publicationAbstract;
    private String content;
    private String contentLink;
    private String originalFilename;
    private byte[] originalFileContent;
    private boolean pushToArxiv;
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public Set<Long> getBranchIds() {
        return branchIds;
    }
    public void setBranchIds(Set<Long> branchIds) {
        this.branchIds = branchIds;
    }
    public Set<UUID> getAuthorIds() {
        return authorIds;
    }
    public void setAuthorIds(Set<UUID> authorIds) {
        this.authorIds = authorIds;
    }
    public Set<String> getNonRegisteredAuthors() {
        return nonRegisteredAuthors;
    }
    public void setNonRegisteredAuthors(Set<String> nonRegisteredAuthors) {
        this.nonRegisteredAuthors = nonRegisteredAuthors;
    }
    public PublicationStatus getStatus() {
        return status;
    }
    public void setStatus(PublicationStatus status) {
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
    public String getPublicationAbstract() {
        return publicationAbstract;
    }
    public void setPublicationAbstract(String publicationAbstract) {
        this.publicationAbstract = publicationAbstract;
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
    public boolean isPushToArxiv() {
        return pushToArxiv;
    }
    public void setPushToArxiv(boolean pushToArxiv) {
        this.pushToArxiv = pushToArxiv;
    }
    public String getContentLink() {
        return contentLink;
    }
    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }
    public Long getPrimaryBranch() {
        return primaryBranch;
    }
    public void setPrimaryBranch(Long primaryBranch) {
        this.primaryBranch = primaryBranch;
    }
}
