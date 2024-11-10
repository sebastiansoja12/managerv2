package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

public class DeviceVersionRepositoryImpl implements DeviceVersionRepository {

    private final DeviceReadRepository deviceReadRepository;

    private final DeviceVersionReadRepository deviceVersionReadRepository;

    public DeviceVersionRepositoryImpl(final DeviceReadRepository deviceReadRepository,
                                       final DeviceVersionReadRepository deviceVersionReadRepository) {
        this.deviceReadRepository = deviceReadRepository;
        this.deviceVersionReadRepository = deviceVersionReadRepository;
    }

    @Override
    public boolean updateRequired(final TerminalId terminalId) {
        final DeviceEntity deviceEntity = this.deviceReadRepository
                .findById(terminalId.getValue())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        final DeviceVersionEntity deviceVersionEntity = this.deviceVersionReadRepository
                .findByDeviceId(terminalId.getValue())
                .orElseThrow(() -> new RuntimeException("Device version not found"));
        return !deviceVersionEntity.getVersion().equals(deviceEntity.getVersion());
    }
}
