package com.warehouse.returntoken.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;

public interface ReturnTokenReadRepository {
    Optional<ReturnTokenEntity> findByShipmentId(final ShipmentId shipmentId);
    Boolean existsByReturnToken(final String value);
}
