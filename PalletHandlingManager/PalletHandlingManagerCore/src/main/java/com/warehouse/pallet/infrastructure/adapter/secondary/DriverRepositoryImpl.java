package com.warehouse.pallet.infrastructure.adapter.secondary;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.port.secondary.DriverRepository;
import com.warehouse.pallet.domain.vo.DriverId;

public class DriverRepositoryImpl implements DriverRepository {

    @Override
    public void create(final Driver driver) {

    }

    @Override
    public void update(final Driver driver) {

    }

    @Override
    public Driver findById(final DriverId driverId) {
        return null;
    }
}
