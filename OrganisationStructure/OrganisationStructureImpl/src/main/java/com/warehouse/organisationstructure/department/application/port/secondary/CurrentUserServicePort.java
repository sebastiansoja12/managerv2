package com.warehouse.organisationstructure.department.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {

    UserId getCurrentUserId();
}
