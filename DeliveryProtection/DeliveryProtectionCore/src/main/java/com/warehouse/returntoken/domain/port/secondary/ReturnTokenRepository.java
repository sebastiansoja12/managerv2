package com.warehouse.returntoken.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;

import java.util.Optional;

public interface ReturnTokenRepository {
    Optional<ReturnTokenEntity> findByShipmentId(final ShipmentId shipmentId);
    Boolean exists(final String value);

    void create(final ReturnToken returnToken);
}
