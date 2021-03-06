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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Publication.getLatestByBranch",
                query = "SELECT p FROM Publication p WHERE :branch IN elements(p.branches) OR :branch IN elements(p.topLevelBranches) ORDER BY p.created"),
        @NamedQuery(name = "Publication.getRevisions",
                query = "SELECT r FROM PublicationRevision r WHERE r.publication = :publication"),
            @NamedQuery(name = "Publication.getPublicationsOfUser",
                query = "SELECT p FROM Publication p WHERE :user in elements(p.authors)") })
@Table(name="publications")
@Audited
public class Publication extends BaseTimedEntity {

    @Id
    private String uri;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "publication_authors")
    private Set<User> authors = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> nonRegisteredAuthors = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    private PublicationRevision currentRevision;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    private PublicationRevision currentDraft;

    @Enumerated(EnumType.STRING)
    private PublicationStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<Branch> branches = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "publication_top_level_branches")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<Branch> topLevelBranches = new HashSet<>();

    @ManyToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Branch primaryBranch;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="publication_tags")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    private Publication followUpTo;

    @Column
    private String followUpToLink;

    @Column
    private String followUpToDoi;

    @Column(nullable = false)
    private int citations;

    @Column(nullable = false)
    private int reviews;

    @Column(nullable = false)
    private int preliminaryReviewScore;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private PublicationSource source;

    @Column
    private String doi;

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

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
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

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public Set<Branch> getParentBranches() {
        return topLevelBranches;
    }

    public void setParentBranches(Set<Branch> parentBranches) {
        this.topLevelBranches = parentBranches;
    }

    public PublicationRevision getCurrentDraft() {
        return currentDraft;
    }

    public void setCurrentDraft(PublicationRevision currentDraft) {
        this.currentDraft = currentDraft;
    }

    @Override
    public String toString() {
        return "Publication [uri=" + uri + ", authors=" + authors + ", nonRegisteredAuthors=" + nonRegisteredAuthors
                + ", currentRevision=" + currentRevision + ", created=" + getCreated() + ", status=" + status
                + ", branches=" + branches + ", primaryBranch=" + primaryBranch + ", tags=" + tags + ", followUpTo="
                + followUpTo + ", followUpToLink=" + followUpToLink + ", followUpToDoi=" + followUpToDoi
                + ", citations=" + citations + ", language=" + language + ", source=" + source + "]";
    }
}
