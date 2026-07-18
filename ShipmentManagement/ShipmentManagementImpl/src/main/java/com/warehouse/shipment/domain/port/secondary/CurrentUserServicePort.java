package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.vo.UserContext;

public interface CurrentUserServicePort {
    UserId getCurrentUserId();
    UserContext getCurrentUserContext();
}
