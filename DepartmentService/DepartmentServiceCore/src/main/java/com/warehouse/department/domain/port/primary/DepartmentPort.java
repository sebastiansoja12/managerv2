package com.warehouse.department.domain.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateRequest;
import com.warehouse.department.domain.vo.*;

import java.util.List;

public interface DepartmentPort {

    Department findByDepartmentCode(DepartmentCode departmentCode);

    List<Department> findAll();

    DepartmentCreateResponse createDepartments(final DepartmentCreateRequest request);

    IdentificationNumberChangeResponse changeIdentificationNumber(final IdentificationNumberChangeRequest request);

    void changeAddress(final UpdateAddressRequest request);

    void changeDepartmentActive(final DepartmentCode departmentCodeValue, final Boolean active);

    void changeDepartmentType(final DepartmentCode departmentCodeValue, final DepartmentType departmentType);

    void changeAdminUser(final DepartmentCode departmentCode, final UserId userId);

    void changeStatus(final ChangeDepartmentStatusRequest request);
}
