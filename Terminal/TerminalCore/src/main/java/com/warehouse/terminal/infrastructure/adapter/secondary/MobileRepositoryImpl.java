package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;

public class MobileRepositoryImpl implements DeviceRepository<Mobile> {

    private final MobileReadRepository repository;

    public MobileRepositoryImpl(final MobileReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mobile findById(final DeviceId deviceId) {
        return null;
    }

    @Override
    public void saveOrUpdate(final Mobile device) {
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public boolean canHandle(final DeviceType type) {
        return DeviceType.MOBILE.equals(type);
    }

    @Override
    public DeviceId nextDeviceId() {
        return null;
    }
}
