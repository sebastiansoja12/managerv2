package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.vo.DriverId;

public interface DriverRepository {
    void create(final Driver driver);
    void update(final Driver driver);
    Driver findById(final DriverId driverId);
}
