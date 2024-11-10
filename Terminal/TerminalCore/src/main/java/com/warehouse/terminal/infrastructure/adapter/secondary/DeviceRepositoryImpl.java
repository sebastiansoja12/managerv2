package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;

public class DeviceRepositoryImpl<T> implements DeviceRepository {

    private final DeviceReadRepository deviceReadRepository;

    public DeviceRepositoryImpl(final DeviceReadRepository deviceReadRepository) {
        this.deviceReadRepository = deviceReadRepository;
    }

    @Override
    public Object findById(final TerminalId terminalId) {
        return deviceReadRepository.findById(terminalId.getValue()).orElseThrow(RuntimeException::new);
    }

    @Override
    public Object saveOrUpdate(final Terminal terminal) {
        final DeviceEntity deviceEntity = DeviceEntity.from(terminal);
        return this.deviceReadRepository.save(deviceEntity);
    }
}
