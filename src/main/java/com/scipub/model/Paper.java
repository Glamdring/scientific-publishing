package com.scipub.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.joda.time.DateTime;

@Entity
public class Paper {

    @Id
    private String doi;
    
    @ManyToMany(fetch=FetchType.EAGER)
    private List<User> authors;
    
    @ManyToMany(fetch=FetchType.EAGER)
    private List<String> nonRegisteredAuthors;
    
    private DateTime lastModified;
}
