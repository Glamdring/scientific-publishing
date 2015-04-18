package com.scipub.dto;

public class UserDetails {

    private String id;
    private String firstName;
    private String lastName;
    private String degree;
    private String organization;
    private String smallPhotoUri;
    private String largePhotoUri;
    
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
}
