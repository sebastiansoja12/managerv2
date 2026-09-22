package com.warehouse.department.domain.port.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.domain.model.Department;

import java.util.List;

public interface DepartmentRepository {

    void createOrUpdate(final Department department);

    Department findByDepartmentCode(final DepartmentCode departmentCode);

    Department findByDepartmentCodeIncludingArchived(final DepartmentCode departmentCode);

    Department findByDepartmentId(final DepartmentId departmentId);

    List<Department> findAll();

    List<Department> findAllArchived();

    Boolean checkExists(final DepartmentCode departmentCode);

    boolean checkExistsAnyWithCountryCode(final CountryCode countryCode);
}
