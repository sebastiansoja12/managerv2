package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

public interface UserServicePort {

    String getUsername(final UserId userId);
}
