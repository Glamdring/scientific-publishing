package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Inline comment valid for a particular revision of a publication
 * @author bozhanov
 */
@Entity
@Table(name = "inline_comments")
public class InlineComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private PublicationRevision revision;
    
    @ManyToOne
    private User commenter;
    
    @Type(type="com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PublicationRevision getRevision() {
        return revision;
    }

    public void setRevision(PublicationRevision revision) {
        this.revision = revision;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
