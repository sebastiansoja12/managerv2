package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.port.secondary.DriverRepository;
import com.warehouse.pallet.domain.vo.DriverId;
import org.springframework.stereotype.Service;

@Service
public class DriverStorageServiceImpl implements DriverStorageService {

    private final DriverRepository driverRepository;

    public DriverStorageServiceImpl(final DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver find(final DriverId driverId) {
        return null;
    }
}
