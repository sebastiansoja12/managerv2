package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.vo.DriverId;

public interface DriverStorageService {
    Driver find(final DriverId driverId);
}
