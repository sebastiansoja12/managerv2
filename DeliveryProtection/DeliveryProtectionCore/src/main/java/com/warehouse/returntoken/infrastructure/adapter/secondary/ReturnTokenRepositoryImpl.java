package com.warehouse.returntoken.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.port.secondary.ReturnTokenRepository;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenRepositoryImpl implements ReturnTokenRepository {

    private final ReturnTokenReadRepository repository;

    @Override
    public Optional<ReturnTokenEntity> findByShipmentId(final ShipmentId shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }

    @Override
    public Boolean exists(final String value) {
        return repository.existsByReturnToken(value);
    }
}
