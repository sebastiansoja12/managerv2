package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Objects;

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

    private static final long MOBILE_ID_PREFIX = 303_000_000_000_000_000L;

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
        final long maxId = repository.findAll().stream()
                .map(MobileEntity::getId)
                .filter(Objects::nonNull)
                .mapToLong(DeviceId::getValue)
                .max()
                .orElse(MOBILE_ID_PREFIX);
        return new DeviceId(maxId >= MOBILE_ID_PREFIX ? maxId + 1 : MOBILE_ID_PREFIX + 1);
    }
}
