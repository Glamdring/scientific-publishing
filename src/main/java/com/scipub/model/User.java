/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name="users")
@Audited
public class User extends BaseTimedEntity implements Serializable {

    private static final long serialVersionUID = 978398160453380686L;

    @Id
    @Type(type="uuid-char")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_branches")
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
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
        return  firstName + " " + lastName + (degree != null ? ", " + degree : "");
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

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", orcid=" + orcid + ", firstName=" + firstName
                + ", middleName=" + middleName + ", lastName=" + lastName + ", degree=" + degree + ", organization="
                + organization + ", smallPhotoUri=" + smallPhotoUri + ", largePhotoUri=" + largePhotoUri + ", bio="
                + bio + ", arxivUsername=" + arxivUsername + ", arxivPassword=" + arxivPassword + ", loginToken="
                + loginToken + ", loginSeries=" + loginSeries + ", lastLoginTime=" + lastLoginTime
                + ", loginAutomatically=" + loginAutomatically + ", branches=" + branches + "]";
    }
    
}
