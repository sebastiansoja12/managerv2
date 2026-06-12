package com.warehouse.routetracker.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {

    UserId getCurrentUserId();

    OperatorId getCurrentOperatorId();

    DepartmentId getCurrentDepartmentId();
}
