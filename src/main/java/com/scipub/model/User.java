package com.scipub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class User {

    @Id
    private String id;
    
    @Column(unique = true)
    private String email;
    
    @Column
    private String orcid;
    
    @Column
    private String names;
    
    @ManyToOne
    private Organization organization;
    
    @Column
    private String photo;
    
    @Lob
    private String bio;

    @Column
    private String arxivUsername;
    
    @Column
    private String arxivPassword;
    
    /**
     * Login token is used for secure cookie-based login
     */
    @Column
    private String loginToken;

    /**
     * Login series is used for secure cookie-based login
     */
    @Column
    private String loginSeries;
    
    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime lastLoginTime;
    
    @Type(type = "com.scipub.util.PersistentDateTime")
    private DateTime registrationTime;
    
    @Column(nullable=false)
    private boolean loginAutomatically;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getArxivUsername() {
        return arxivUsername;
    }

    public void setArxivUsername(String arxivUsername) {
        this.arxivUsername = arxivUsername;
    }

    public String getArxivPassword() {
        return arxivPassword;
    }

    public void setArxivPassword(String arxivPassword) {
        this.arxivPassword = arxivPassword;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginSeries() {
        return loginSeries;
    }

    public void setLoginSeries(String loginSeries) {
        this.loginSeries = loginSeries;
    }

    public DateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(DateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public DateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(DateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public boolean isLoginAutomatically() {
        return loginAutomatically;
    }

    public void setLoginAutomatically(boolean loginAutomatically) {
        this.loginAutomatically = loginAutomatically;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
