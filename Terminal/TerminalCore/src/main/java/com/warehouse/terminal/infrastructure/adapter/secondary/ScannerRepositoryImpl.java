package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Objects;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.domain.port.secondary.DeviceRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.EntityToModelMapper;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.ModelToEntityMapper;


import org.springframework.stereotype.Repository;


@Repository
public class ScannerRepositoryImpl implements DeviceRepository<Scanner> {

    private static final long SCANNER_ID_PREFIX = 202_000_000_000_000_000L;

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
        final long maxId = repository.findAll().stream()
                .map(ScannerEntity::getId)
                .filter(Objects::nonNull)
                .mapToLong(DeviceId::getValue)
                .max()
                .orElse(SCANNER_ID_PREFIX);
        return new DeviceId(maxId >= SCANNER_ID_PREFIX ? maxId + 1 : SCANNER_ID_PREFIX + 1);
    }
}
