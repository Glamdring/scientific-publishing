package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

@Entity
public class PaperRevision {

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;
    
    @ManyToOne
    private Paper paper;
    
    @Column(nullable=false)
    private int revision;
    
    @Lob
    private String content;
    
    @Column(nullable=false)
    private boolean latest;
    
    private DateTime created;
    
}
