package com.warehouse.auth.domain.model;

public class User extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
