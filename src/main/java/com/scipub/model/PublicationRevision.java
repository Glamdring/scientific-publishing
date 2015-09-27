package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="publication_revisions")
public class PublicationRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User submitter;
    
    @Column(nullable = false)
    private int revision;

    @Column
    private String title;
    
    @Column(length=5000)
    private String publicationAbstract;
    
    @Lob
    private String content;

    @Column
    private String contentLink;
    
    @Column(nullable = false)
    private boolean latestPublished;

    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime created;

    @Column
    private String originalFilename;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
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

    public void setPublicationAbstract(String paperAbstract) {
        this.publicationAbstract = paperAbstract;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLatestPublished() {
        return latestPublished;
    }

    public void setLatestPublished(boolean latest) {
        this.latestPublished = latest;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    @Override
    public String toString() {
        return "PublicationRevision [id=" + id + ", publication=" + publication + ", submitter=" + submitter
                + ", revision=" + revision + ", title=" + title + ", publicationAbstract=" + publicationAbstract
                + ", content=" + content + ", contentLink=" + contentLink + ", latestPublished=" + latestPublished
                + ", created=" + created + ", originalFilename=" + originalFilename + "]";
    }
}
