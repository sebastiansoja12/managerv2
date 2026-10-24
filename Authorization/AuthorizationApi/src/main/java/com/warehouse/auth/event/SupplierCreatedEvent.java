package com.warehouse.auth.event;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.function.Consumer;

public class SupplierCreatedEvent {
    private final SupplierId supplierId;
    private final SupplierCode supplierCode;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final DepartmentId departmentId;
    private final Consumer<UserId> userCreatedId;

    public SupplierCreatedEvent(final SupplierId supplierId,
                                final SupplierCode supplierCode,
                                final String firstName,
                                final String lastName,
                                final String email,
                                final DepartmentId departmentId,
                                final Consumer<UserId> userCreatedId) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departmentId = departmentId;
        this.userCreatedId = userCreatedId;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public Consumer<UserId> getUserCreatedId() {
        return userCreatedId;
    }
}
