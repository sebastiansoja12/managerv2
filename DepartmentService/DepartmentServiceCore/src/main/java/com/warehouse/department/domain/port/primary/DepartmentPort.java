package com.warehouse.department.domain.port.primary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.vo.*;

import java.util.List;

public interface DepartmentPort {

    Department findByDepartmentCode(DepartmentCode departmentCode);

    List<Department> findAll();

    DepartmentCreateResponse createDepartments(final DepartmentCreateRequest request);

    void changeAddress(final UpdateAddressRequest request);

    IdentificationNumberChangeResponse changeIdentificationNumber(final IdentificationNumberChangeRequest request);
}
