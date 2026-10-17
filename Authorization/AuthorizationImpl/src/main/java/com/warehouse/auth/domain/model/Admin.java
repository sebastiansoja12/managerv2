package com.warehouse.auth.domain.model;

public class Admin extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
