package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;

public class ScannerRepositoryImpl implements DeviceRepository<Scanner> {

    private final ScannerReadRepository repository;

    public ScannerRepositoryImpl(final ScannerReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Scanner findById(final DeviceId deviceId) {
        return null;
    }

    @Override
    public void saveOrUpdate(final Scanner scanner) {
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public boolean canHandle(final DeviceType type) {
        return DeviceType.SCANNER.equals(type);
    }

    @Override
    public DeviceId nextDeviceId() {
        return null;
    }
}
