package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface UserServicePort {

    String getUsername(final UserId userId);
}
