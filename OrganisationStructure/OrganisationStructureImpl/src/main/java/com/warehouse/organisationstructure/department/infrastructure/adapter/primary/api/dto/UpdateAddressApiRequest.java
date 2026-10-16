package com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto;

public record UpdateAddressApiRequest(DepartmentCodeApi departmentCode, AddressApi address) {


    public String getResourceName() {
        return this.getClass().getSimpleName();
    }
}
