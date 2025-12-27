package com.warehouse.department.domain.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.model.DepartmentCreateCommand;
import com.warehouse.department.domain.vo.*;

import java.util.List;

public interface DepartmentPort {

    Department findByDepartmentCode(final DepartmentCode departmentCode);

    List<Department> findAll();

    DepartmentCreateResponse createDepartments(final DepartmentCreateCommand command);

    IdentificationNumberChangeResponse changeIdentificationNumber(final IdentificationNumberChangeCommand command);

    void changeAddress(final UpdateAddressCommand command);

    void changeDepartmentActive(final DepartmentCode departmentCodeValue, final Boolean active);

    void changeDepartmentType(final DepartmentCode departmentCodeValue, final DepartmentType departmentType);

    void changeAdminUser(final DepartmentCode departmentCode, final UserId userId);

    void changeStatus(final ChangeDepartmentStatusCommand command);

    void changeEmail(final DepartmentCode departmentCode, final String email);
}
