package com.warehouse.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;

public interface DepartmentService {
    void createDepartment(final Department department);

    Department findByDepartmentCode(final DepartmentCode departmentCode);

    void changeAddress(final DepartmentCode departmentCode, final Address address);

    void changeIdentificationNumber(final DepartmentCode departmentCode, final String newIdentificationNumber);

    void activateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy);

    void deactivateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy);

    void changeDepartmentType(final DepartmentCode departmentCode, final DepartmentType departmentType);
}
