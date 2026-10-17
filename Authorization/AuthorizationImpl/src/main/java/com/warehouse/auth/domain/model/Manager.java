package com.warehouse.auth.domain.model;

public class Manager extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.MANAGER;
    }
}
