package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Resource {

    @Id
    private String uri;
    
    @Column
    private String filename;
    
    @ManyToOne
    private Paper paper;
    
    @Column
    private String link; //figshare, slideshare, github,...

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    public String getUri() {
        return uri;
    }

    public void setUri(String doi) {
        this.uri = doi;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
