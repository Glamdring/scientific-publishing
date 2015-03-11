package com.scipub.dto;

import java.util.List;

import org.springframework.social.connect.Connection;

public class RegistrationDto {
    
    private String email;
    private String firstName;
    private String lastName;
    private String degree;
    private Connection<?> connection;
    private boolean loginAutomatically;
    private List<Long> branchIds;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public Connection<?> getConnection() {
        return connection;
    }
    public void setConnection(Connection<?> connection) {
        this.connection = connection;
    }
    public boolean isLoginAutomatically() {
        return loginAutomatically;
    }
    public void setLoginAutomatically(boolean loginAutomatically) {
        this.loginAutomatically = loginAutomatically;
    }
    public List<Long> getBranchIds() {
        return branchIds;
    }
    public void setBranchIds(List<Long> branchIds) {
        this.branchIds = branchIds;
    }
}
