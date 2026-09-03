package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;

@Repository
public class TerminalRepositoryImpl implements DeviceRepository<Terminal> {
    private static final String TERMINAL_ID_PREFIX = "TL:";

    private final OperatorFilteredRepository<TerminalEntity> repository;

    private final NoOperatorContextRepository<TerminalEntity> noOperatorContextRepository;

    private final ModelToEntityMapper toEntityMapper;

    private final EntityToModelMapper toModelMapper;

    public TerminalRepositoryImpl(final OperatorFilteredRepository<TerminalEntity> repository,
                                  final NoOperatorContextRepository<TerminalEntity> noOperatorContextRepository,
                                  final ModelToEntityMapper toEntityMapper,
                                  final EntityToModelMapper toModelMapper) {
        this.repository = repository;
        this.noOperatorContextRepository = noOperatorContextRepository;
        this.toEntityMapper = toEntityMapper;
        this.toModelMapper = toModelMapper;
    }

    @Override
    public Terminal findById(final DeviceId deviceId) {
        return findEntity(deviceId)
                .map(toModelMapper::map)
                .orElseThrow();
    }

    @Override
    public Optional<Terminal> findByIdForAuthentication(final DeviceId deviceId) {
        return noOperatorContextRepository.createCriteria(TerminalEntity.class)
                .eq("deviceId.value", deviceId.value())
                .one()
                .map(toModelMapper::map);
    }

    @Override
    public Optional<Terminal> findByExternalSystemId(final String externalSystemId) {
        return repository.createCriteria(TerminalEntity.class)
                .eq("identity.externalSystemId", externalSystemId)
                .one()
                .map(toModelMapper::map);
    }

    @Override
    public List<Terminal> findByUserId(final UserId userId) {
        return repository.createCriteria(TerminalEntity.class)
                .eq("userId.value", userId.value())
                .list()
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public void saveOrUpdate(final Terminal terminal) {
        final TerminalEntity entity = toEntityMapper.map(terminal);
        if (findEntity(terminal.getDeviceId()).isPresent()) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    @Override
    public List<Terminal> findAll() {
        return repository.createCriteria(TerminalEntity.class)
                .list()
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

    private Optional<TerminalEntity> findEntity(final DeviceId deviceId) {
        return repository.createCriteria(TerminalEntity.class)
                .eq("deviceId.value", deviceId.value())
                .one();
    }
}
