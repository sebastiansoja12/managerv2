package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;

import java.util.Map;
import java.util.Set;

public interface ShipmentActorServicePort {

    UserId currentUserId();

    Map<UserId, String> findUsernames(final Set<UserId> userIds);
}
