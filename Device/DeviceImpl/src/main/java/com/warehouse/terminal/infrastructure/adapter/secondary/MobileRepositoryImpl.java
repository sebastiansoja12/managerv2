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
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;

@Repository
public class MobileRepositoryImpl implements DeviceRepository<Mobile> {
    private static final String MOBILE_ID_PREFIX = "MB:";

    private final OperatorFilteredRepository<MobileEntity> repository;

    private final NoOperatorContextRepository<MobileEntity> noOperatorContextRepository;

    private final ModelToEntityMapper toEntityMapper;

    private final EntityToModelMapper toModelMapper;

    public MobileRepositoryImpl(final OperatorFilteredRepository<MobileEntity> repository,
                                final NoOperatorContextRepository<MobileEntity> noOperatorContextRepository,
                                final ModelToEntityMapper toEntityMapper,
                                final EntityToModelMapper toModelMapper) {
        this.repository = repository;
        this.noOperatorContextRepository = noOperatorContextRepository;
        this.toEntityMapper = toEntityMapper;
        this.toModelMapper = toModelMapper;
    }

    @Override
    public Mobile findById(final DeviceId deviceId) {
        return findEntity(deviceId)
                .map(toModelMapper::map)
                .orElseThrow();
    }

    @Override
    public Optional<Mobile> findByIdForAuthentication(final DeviceId deviceId) {
        return noOperatorContextRepository.createCriteria(MobileEntity.class)
                .eq("deviceId.value", deviceId.value())
                .one()
                .map(toModelMapper::map);
    }

    @Override
    public Optional<Mobile> findByExternalSystemId(final String externalSystemId) {
        return repository.createCriteria(MobileEntity.class)
                .eq("identity.externalSystemId", externalSystemId)
                .one()
                .map(toModelMapper::map);
    }

    @Override
    public List<Mobile> findByUserId(final UserId userId) {
        return repository.createCriteria(MobileEntity.class)
                .eq("userId.value", userId.value())
                .list()
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public void saveOrUpdate(final Mobile mobile) {
        final MobileEntity entity = toEntityMapper.map(mobile);
        if (findEntity(mobile.getDeviceId()).isPresent()) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    @Override
    public List<Mobile> findAll() {
        return repository.createCriteria(MobileEntity.class)
                .list()
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public boolean canHandle(final DeviceType type) {
        return DeviceType.MOBILE.equals(type);
    }

    @Override
    public DeviceId nextDeviceId() {
        return new DeviceId(MOBILE_ID_PREFIX + UUID.randomUUID());
    }

    private Optional<MobileEntity> findEntity(final DeviceId deviceId) {
        return repository.createCriteria(MobileEntity.class)
                .eq("deviceId.value", deviceId.value())
                .one();
    }
}
