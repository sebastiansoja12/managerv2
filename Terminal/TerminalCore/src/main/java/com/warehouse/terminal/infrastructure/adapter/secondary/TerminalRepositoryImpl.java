package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.UUID;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;


import org.springframework.stereotype.Repository;


@Repository
public class TerminalRepositoryImpl implements DeviceRepository<Terminal> {
    private static final String TERMINAL_ID_PREFIX = "TL:";

    private final TerminalReadRepository repository;

    private final ModelToEntityMapper toEntityMapper;

    private final EntityToModelMapper toModelMapper;

    public TerminalRepositoryImpl(final TerminalReadRepository repository,
                                  final ModelToEntityMapper toEntityMapper,
                                  final EntityToModelMapper toModelMapper) {
        this.repository = repository;
        this.toEntityMapper = toEntityMapper;
        this.toModelMapper = toModelMapper;
    }

    @Override
    public Terminal findById(final DeviceId deviceId) {
        return repository.findById(deviceId)
                .map(toModelMapper::map)
                .orElseThrow();
    }

    @Override
    public void saveOrUpdate(final Terminal terminal) {
        final TerminalEntity terminalEntity = toEntityMapper.map(terminal);
        repository.save(terminalEntity);
    }

    @Override
    public List<Terminal> findAll() {
        return repository.findAll()
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public boolean canHandle(final DeviceType type) {
        return DeviceType.TERMINAL.equals(type);
    }

    @Override
    public DeviceId nextDeviceId() {
        return new DeviceId(TERMINAL_ID_PREFIX + UUID.randomUUID());
    }
}
