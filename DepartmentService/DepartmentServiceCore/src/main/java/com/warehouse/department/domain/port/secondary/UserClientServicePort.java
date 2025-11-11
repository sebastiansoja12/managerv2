package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public interface UserClientServicePort {
    void update(final DepartmentSnapshot snapshot);
}
