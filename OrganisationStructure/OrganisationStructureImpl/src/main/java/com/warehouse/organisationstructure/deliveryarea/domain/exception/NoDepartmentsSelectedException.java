package com.warehouse.organisationstructure.deliveryarea.domain.exception;

public class NoDepartmentsSelectedException extends DeliveryAreaException {

    public NoDepartmentsSelectedException() {
        super(400, "NO_DEPARTMENTS", "Select at least one department");
    }
}
