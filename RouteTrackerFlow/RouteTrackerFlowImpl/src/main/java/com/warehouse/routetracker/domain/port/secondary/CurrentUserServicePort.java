package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;

public interface CurrentUserServicePort {

    UserId getCurrentUserId();

    OperatorId getCurrentOperatorId();

    DepartmentId getCurrentDepartmentId();
}
