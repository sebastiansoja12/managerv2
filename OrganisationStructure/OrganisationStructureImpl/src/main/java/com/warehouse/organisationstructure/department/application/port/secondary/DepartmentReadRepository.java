package com.warehouse.organisationstructure.department.application.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

public interface DepartmentReadRepository<T> {
    void sync(final DepartmentSnapshot snapshot);
    List<T> list();
    List<T> listArchived();
    T findByDepartmentCode(final DepartmentCode departmentCode);
    T findByDepartmentCodeIncludingArchived(final DepartmentCode departmentCode);
    T findByDepartmentId(final DepartmentId departmentId);
}
