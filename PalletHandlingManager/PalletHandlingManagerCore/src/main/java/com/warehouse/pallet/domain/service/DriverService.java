package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.vo.DepartmentId;

public interface DriverService {
    void createDriver(final Driver driver);
    void changeFirstName(final Driver driver, final String firstName);
    void changeLastName(final Driver driver, final String lastName);
    void assignDepartment(final Driver driver, final DepartmentId departmentId);
}
