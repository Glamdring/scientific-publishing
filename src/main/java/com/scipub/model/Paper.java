package com.scipub.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Paper {

    @Id
    private String uri;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> authors;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<String> nonRegisteredAuthors;

    @ManyToOne
    private PaperRevision currentRevision;

    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime created;

    @Enumerated(EnumType.STRING)
    private PaperStatus status;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Branch> branches;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;
    
    @ManyToOne
    private Paper followUpTo;
    
    @Column
    private String followUpToUri;
    
    @Column
    private int citations;

    public String getUri() {
        return uri;
    }

    public void setUri(String doi) {
        this.uri = doi;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }

    public List<String> getNonRegisteredAuthors() {
        return nonRegisteredAuthors;
    }

    public void setNonRegisteredAuthors(List<String> nonRegisteredAuthors) {
        this.nonRegisteredAuthors = nonRegisteredAuthors;
    }

    public PaperRevision getCurrentRevision() {
        return currentRevision;
    }

    public void setCurrentRevision(PaperRevision currentRevision) {
        this.currentRevision = currentRevision;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public PaperStatus getStatus() {
        return status;
    }

    public void setStatus(PaperStatus status) {
        this.status = status;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Paper getFollowUpTo() {
        return followUpTo;
    }

    public void setFollowUpTo(Paper followUpTo) {
        this.followUpTo = followUpTo;
    }

    public String getFollowUpToUri() {
        return followUpToUri;
    }

    public void setFollowUpToUri(String followUpToLink) {
        this.followUpToUri = followUpToLink;
    }

    public int getCitations() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations = citations;
    }
}
