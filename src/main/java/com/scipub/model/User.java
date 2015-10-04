package com.scipub.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User extends BaseTimedEntity implements Serializable {

    private static final long serialVersionUID = 978398160453380686L;

    @Id
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column
    private String orcid;

    @Column
    private String firstName;

    @Column
    private String middleName;

    @Column
    private String lastName;

    @Column
    private String degree;

    @ManyToOne
    private Organization organization;

    @Column
    private String smallPhotoUri;
    
    @Column
    private String largePhotoUri;

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

    private LocalDateTime lastLoginTime;

    @Column(nullable = false)
    private boolean loginAutomatically;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_branches")
    private Set<Branch> branches = new HashSet<>();

    // deliberately no "gender" - it's irrelevant
    
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getSmallPhotoUri() {
        return smallPhotoUri;
    }

    public void setSmallPhotoUri(String smallPhotoUri) {
        this.smallPhotoUri = smallPhotoUri;
    }

    public String getLargePhotoUri() {
        return largePhotoUri;
    }

    public void setLargePhotoUri(String largePhotoUri) {
        this.largePhotoUri = largePhotoUri;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID userId) {
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

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isLoginAutomatically() {
        return loginAutomatically;
    }

    public void setLoginAutomatically(boolean loginAutomatically) {
        this.loginAutomatically = loginAutomatically;
    }

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
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
