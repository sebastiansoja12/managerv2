package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public interface UserClientServicePort {
    void notifyUserDepartmentDeleted(final DepartmentSnapshot snapshot);
    void notifyUserDepartmentActivated(final DepartmentSnapshot snapshot);
    void notifyUserDepartmentArchived(final DepartmentSnapshot snapshot);
    void notifyUserDepartmentSuspended(final DepartmentSnapshot snapshot);
    void notifyUserDepartmentChanged(final DepartmentSnapshot snapshot);
}
