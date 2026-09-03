package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.time.Instant;

public class PlatformOwner implements Account {

    private final PlatformOwnerId platformOwnerId;

    private final String username;

    private final String password;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final Boolean deleted;

    private final Instant createdAt;

    private final Instant updatedAt;

    public PlatformOwner(final PlatformOwnerId platformOwnerId,
                         final String username,
                         final String password,
                         final String firstName,
                         final String lastName,
                         final String email,
                         final Boolean deleted,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.platformOwnerId = platformOwnerId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PlatformOwner create(final PlatformOwnerId platformOwnerId,
                                       final String username,
                                       final String encodedPassword,
                                       final String firstName,
                                       final String lastName,
                                       final String email) {
        final Instant now = Instant.now();
        return new PlatformOwner(platformOwnerId, username, encodedPassword, firstName, lastName, email, false, now, now);
    }

    public PlatformOwner block() {
        return new PlatformOwner(platformOwnerId, username, password, firstName, lastName, email, true, createdAt,
                Instant.now());
    }

    public PlatformOwner unblock() {
        return new PlatformOwner(platformOwnerId, username, password, firstName, lastName, email, false, createdAt,
                Instant.now());
    }

    public PlatformOwner changePassword(final String encodedPassword) {
        return new PlatformOwner(platformOwnerId, username, encodedPassword, firstName, lastName, email, deleted,
                createdAt, Instant.now());
    }

    public boolean isActive() {
        return !Boolean.TRUE.equals(deleted);
    }

    public PlatformOwnerId getPlatformOwnerId() {
        return platformOwnerId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Boolean isDeleted() {
        return deleted;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    @Override
    public AccountLevel getLevel() {
        return AccountLevel.PLATFORM_OWNER;
    }

    @Override
    public boolean canAccessOperatorPanel() {
        return isActive();
    }

    @Override
    public boolean canAccessDepartment(final DepartmentId departmentId) {
        return true;
    }
}
