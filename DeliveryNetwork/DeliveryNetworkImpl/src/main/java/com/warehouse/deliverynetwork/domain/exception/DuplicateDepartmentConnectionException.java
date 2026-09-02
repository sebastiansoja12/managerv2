package com.warehouse.deliverynetwork.domain.exception;

import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;

public class DuplicateDepartmentConnectionException extends RuntimeException {

    public DuplicateDepartmentConnectionException(final DepartmentConnection connection) {
        super("Duplicate department connection: "
                + connection.firstDepartmentId().getValue()
                + " <-> "
                + connection.secondDepartmentId().getValue());
    }
}
