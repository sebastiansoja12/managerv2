package com.warehouse.auth.domain.model;

public class Supplier extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.SUPPLIER;
    }
}
