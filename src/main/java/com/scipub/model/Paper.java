package com.scipub.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
    private Set<User> authors = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> nonRegisteredAuthors = new HashSet<>();

    @ManyToOne
    private PaperRevision currentRevision;

    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime created;

    @Enumerated(EnumType.STRING)
    private PaperStatus status;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Branch> branches = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();
    
    @ManyToOne
    private Paper followUpTo;
    
    @Column
    private String followUpToLink;
    
    @Column
    private String followUpToDoi;
    
    @Column
    private int citations;

    public String getUri() {
        return uri;
    }

    public void setUri(String doi) {
        this.uri = doi;
    }

    public Set<User> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<User> authors) {
        this.authors = authors;
    }

    public Set<String> getNonRegisteredAuthors() {
        return nonRegisteredAuthors;
    }

    public void setNonRegisteredAuthors(Set<String> nonRegisteredAuthors) {
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

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Paper getFollowUpTo() {
        return followUpTo;
    }

    public void setFollowUpTo(Paper followUpTo) {
        this.followUpTo = followUpTo;
    }


    public String getFollowUpToLink() {
        return followUpToLink;
    }

    public void setFollowUpToLink(String followUpToLink) {
        this.followUpToLink = followUpToLink;
    }

    public String getFollowUpToDoi() {
        return followUpToDoi;
    }

    public void setFollowUpToDoi(String followUpToDoi) {
        this.followUpToDoi = followUpToDoi;
    }

    public int getCitations() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations = citations;
    }
}
