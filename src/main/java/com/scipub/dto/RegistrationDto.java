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

import java.util.ArrayList;
import java.util.List;

import org.springframework.social.connect.Connection;

public class RegistrationDto {
    
    private String email;
    private String firstName;
    private String lastName;
    private String degree;
    private Connection<?> connection;
    private boolean loginAutomatically;
    private List<Long> branchIds = new ArrayList<>();
    
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
