package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.port.secondary.DevicePairRepository;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public class DevicePairRepositoryImpl implements DevicePairRepository {
    
    private final DevicePairReadRepository repository;

    public DevicePairRepositoryImpl(final DevicePairReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void pair(final Terminal terminal, final DevicePairId devicePairId) {
        final DeviceEntity deviceEntity = DeviceEntity.from(terminal);
		final DevicePairEntity devicePairEntity = new DevicePairEntity(devicePairId, deviceEntity, true, Instant.now(),
				StringUtils.EMPTY);
        this.repository.save(devicePairEntity);
    }
}
