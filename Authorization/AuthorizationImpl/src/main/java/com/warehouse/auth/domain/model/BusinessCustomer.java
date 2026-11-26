package com.warehouse.auth.domain.model;

public class BusinessCustomer extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.BUSINESS_CUSTOMER;
    }
}
