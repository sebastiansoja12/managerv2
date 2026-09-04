package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {
    OperatorId currentOperatorId();
    UserId currentUserId();
}
