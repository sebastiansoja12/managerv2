package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.UserId;

public class FullNameChangeCommand {
    private UserId userId;

    private String firstName;

    private String lastName;

    public FullNameChangeCommand(final String firstName, final String lastName,
                                 final UserId userId) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
