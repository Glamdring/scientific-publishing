package com.scipub.model;

import java.time.LocalDateTime;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;

@Entity
@NamedQueries({
    @NamedQuery(name = "Publication.getLatestByBranch", 
                query = "SELECT p FROM Publication p WHERE :branchId IN elements(p.branches) ORDER BY p.created")
})
public class Publication {

    @Id
    private String uri;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> authors = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> nonRegisteredAuthors = new HashSet<>();

    @ManyToOne
    private PublicationRevision currentRevision;

    @Type(type = "com.scipub.util.PersistentDateTime")
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private PaperStatus status;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Branch> branches = new HashSet<>();
    
    @ManyToOne
    private Branch primaryBranch;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();
    
    @ManyToOne
    private Publication followUpTo;
    
    @Column
    private String followUpToLink;
    
    @Column
    private String followUpToDoi;
    
    @Column
    private int citations;

    @Enumerated(EnumType.STRING)
    private Language language;
    
    @Enumerated(EnumType.STRING)
    private PublicationSource source;
    
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public PublicationRevision getCurrentRevision() {
        return currentRevision;
    }

    public void setCurrentRevision(PublicationRevision currentRevision) {
        this.currentRevision = currentRevision;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
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

    public Publication getFollowUpTo() {
        return followUpTo;
    }

    public void setFollowUpTo(Publication followUpTo) {
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public PublicationSource getSource() {
        return source;
    }

    public void setSource(PublicationSource source) {
        this.source = source;
    }

    public Branch getPrimaryBranch() {
        return primaryBranch;
    }

    public void setPrimaryBranch(Branch primaryBranch) {
        this.primaryBranch = primaryBranch;
    }
}
