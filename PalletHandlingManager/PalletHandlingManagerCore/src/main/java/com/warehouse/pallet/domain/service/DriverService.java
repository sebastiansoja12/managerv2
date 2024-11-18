package com.warehouse.pallet.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.pallet.domain.model.Driver;

public interface DriverService {
    void createDriver(final Driver driver);
    void changeFirstName(final Driver driver, final String firstName);
    void changeLastName(final Driver driver, final String lastName);
    void assignDepartment(final Driver driver, final DepartmentCode departmentCode);
}
