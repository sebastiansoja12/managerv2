package com.warehouse.department.domain.port.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

public interface DepartmentReadRepository<T> {
    void sync(final DepartmentSnapshot snapshot);
    List<T> list();
    T findByDepartmentCode(final DepartmentCode departmentCode);
    T findByDepartmentId(final DepartmentId departmentId);
}
