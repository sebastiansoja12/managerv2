package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.DriverDocument;

import java.util.Optional;

public interface DriverRepository {
    void create(final Driver driver);
    void update(final Driver driver);
    Optional<DriverDocument> findById(final DriverId driverId);
}
