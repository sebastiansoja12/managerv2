package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;


import org.springframework.stereotype.Repository;


@Repository
public class ScannerRepositoryImpl implements DeviceRepository<Scanner> {
    private static final String SCANNER_ID_PREFIX = "SC:";

    private final ScannerReadRepository repository;

    private final ModelToEntityMapper toEntityMapper;

    private final EntityToModelMapper toModelMapper;

    public ScannerRepositoryImpl(final ScannerReadRepository repository,
                                 final ModelToEntityMapper toEntityMapper,
                                 final EntityToModelMapper toModelMapper) {
        this.repository = repository;
        this.toEntityMapper = toEntityMapper;
        this.toModelMapper = toModelMapper;
    }

    @Override
    public Scanner findById(final DeviceId deviceId) {
        final ScannerEntity entity = repository.findById(deviceId).orElseThrow();
        return toModelMapper.map(entity);
    }

    @Override
    public Optional<Scanner> findByExternalSystemId(final String externalSystemId) {
        return repository.findByIdentityExternalSystemId(externalSystemId)
                .map(toModelMapper::map);
    }

    @Override
    public List<Scanner> findByUserId(final UserId userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public void saveOrUpdate(final Scanner scanner) {
        final ScannerEntity entity = toEntityMapper.map(scanner);
        repository.save(entity);
    }

    @Override
    public List<Scanner> findAll() {
        return repository.findAll()
                .stream()
                .map(toModelMapper::map)
                .toList();
    }

    @Override
    public boolean canHandle(final DeviceType type) {
        return DeviceType.SCANNER.equals(type);
    }

    @Override
    public DeviceId nextDeviceId() {
        return new DeviceId(SCANNER_ID_PREFIX + UUID.randomUUID());
    }
}
