package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;

public class DeviceRepositoryImpl<T> implements DeviceRepository {

    private final DeviceReadRepository deviceReadRepository;

    public DeviceRepositoryImpl(final DeviceReadRepository deviceReadRepository) {
        this.deviceReadRepository = deviceReadRepository;
    }

    @Override
    public Object findById(final DeviceId deviceId) {
        return deviceReadRepository
                .findById(deviceId.getValue())
                .map(Terminal::from)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public Object saveOrUpdate(final Terminal terminal) {
        final DeviceEntity deviceEntity = DeviceEntity.from(terminal);
        return this.deviceReadRepository.save(deviceEntity);
    }

    @Override
    public List<Terminal> findAll() {
        return this.deviceReadRepository.findAll().stream().map(Terminal::from).toList();
    }
}
