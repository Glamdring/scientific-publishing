package com.scipub.model;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

@MappedSuperclass
public abstract class BaseTimedEntity {

    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime created;
    
    @Type(type = "com.scipub.util.PersistentLocalDateTime")
    private LocalDateTime modified;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
