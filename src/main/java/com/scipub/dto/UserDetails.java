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
package com.scipub.dto;

import com.scipub.model.User;

public class UserDetails {

    private String id;
    private String firstName;
    private String lastName;
    private String degree;
    private String organization;
    private String smallPhotoUri;
    private String largePhotoUri;
    
    public UserDetails() {
    }
    
    public UserDetails(User entity) {
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.degree = entity.getDegree();
        this.smallPhotoUri = entity.getSmallPhotoUri();
        this.largePhotoUri = entity.getLargePhotoUri();
        if (entity.getOrganization() != null) {
            this.organization = entity.getOrganization().getName();
        }
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
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
    
    public String getDisplayName() {
        return  firstName + " " + lastName + (degree != null ? ", " + degree : ""); 
    }
}
