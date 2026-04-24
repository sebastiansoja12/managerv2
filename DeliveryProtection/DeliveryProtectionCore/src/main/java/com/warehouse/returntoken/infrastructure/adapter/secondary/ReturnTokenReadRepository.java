package com.warehouse.returntoken.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenId;

public interface ReturnTokenReadRepository extends JpaRepository<ReturnTokenEntity, ReturnTokenId> {
    Optional<ReturnTokenEntity> findByShipmentId(final ShipmentId shipmentId);
    Boolean existsByToken(final String token);
}
