package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.port.secondary.DriverRepository;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.DriverDocument;

import java.util.Optional;

public class DriverRepositoryImpl implements DriverRepository {

    @Override
    public void create(final Driver driver) {

    }

    @Override
    public void update(final Driver driver) {

    }

    @Override
    public Optional<DriverDocument> findById(final DriverId driverId) {
        return Optional.empty();
    }
}
