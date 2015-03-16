package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class PublicationRevision {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User submitter;
    
    @Column(nullable = false)
    private int revision;

    @Column
    private String title;
    
    @Column
    private String publicationAbstract;
    
    @Lob
    private String content;

    @Column(nullable = false)
    private boolean latest;

    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime created;

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

    public void setPublication(Publication paper) {
        this.publication = paper;
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

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
