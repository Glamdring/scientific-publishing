package com.scipub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

@Entity
public class Comment {

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;
    
    @ManyToOne
    private Paper paper;
    
    @ManyToOne
    private PeerReview peerReview;
    
    private DateTime created;
    
    @Lob
    private String content;
}
