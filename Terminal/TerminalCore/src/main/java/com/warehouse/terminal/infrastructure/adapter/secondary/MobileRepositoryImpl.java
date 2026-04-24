package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.UUID;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;


import org.springframework.stereotype.Repository;


@Repository
public class MobileRepositoryImpl implements DeviceRepository<Mobile> {
    private static final String MOBILE_ID_PREFIX = "MB:";

    private final MobileReadRepository repository;

    private final ModelToEntityMapper toEntityMapper;

    private final EntityToModelMapper toModelMapper;

    public MobileRepositoryImpl(final MobileReadRepository repository,
                                final ModelToEntityMapper toEntityMapper,
                                final EntityToModelMapper toModelMapper) {
        this.repository = repository;
        this.toEntityMapper = toEntityMapper;
        this.toModelMapper = toModelMapper;
    }

    @Override
    public Mobile findById(final DeviceId deviceId) {
        final MobileEntity entity = repository.findById(deviceId).orElseThrow();
        return toModelMapper.map(entity);
    }

    @Override
    public void saveOrUpdate(final Mobile device) {
        final MobileEntity entity = toEntityMapper.map(device);
        repository.save(entity);
    }

    @Override
    public List<Mobile> findAll() {
        return repository.findAll()
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
}
