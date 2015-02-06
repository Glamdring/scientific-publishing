package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Branch {

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private long id;
    
    @Column
    private String name;
    
    @ManyToOne
    private Branch parentBranch;
}
