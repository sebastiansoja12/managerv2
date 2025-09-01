package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.UserId;

public class FullNameRequest {
    private UserId userId;

    private String firstName;

    private String lastName;

    private String username;

    public FullNameRequest(final String firstName, final String lastName, final String username,
                           final UserId userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
