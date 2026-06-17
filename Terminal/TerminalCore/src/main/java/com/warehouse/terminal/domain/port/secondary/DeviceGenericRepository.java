package com.warehouse.terminal.domain.port.secondary;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.Device;

@Repository
public class DeviceGenericRepository {

    private final Set<DeviceRepository> repositories;

    public DeviceGenericRepository(final Set<DeviceRepository> repositories) {
        this.repositories = repositories;
    }

    public Optional<Device> findById(final DeviceId deviceId) {
        final DeviceRepository repository = determineDeviceRepository(deviceId.type());
        return Optional.ofNullable(repository.findById(deviceId));
    }

    public Optional<Device> findByExternalSystemId(final String externalSystemId) {
        for (final DeviceRepository repository : repositories) {
            final Optional<Device> device = repository.findByExternalSystemId(externalSystemId);
            if (device.isPresent()) {
                return device;
            }
        }
        return Optional.empty();
    }

    public void create(final Device device) {
        final DeviceRepository repository = determineDeviceRepository(device.getDeviceType());
        repository.saveOrUpdate(device);
    }

    public void update(final Device device) {
        final DeviceRepository repository = determineDeviceRepository(device.getDeviceType());
        repository.saveOrUpdate(device);
    }

    public DeviceId nextDeviceId(final DeviceType type) {
        final DeviceRepository repository = determineDeviceRepository(type);
        return repository.nextDeviceId();
    }

    public List findAll() {
        return repositories.stream()
                .flatMap(repository -> repository.findAll().stream())
                .toList();
    }

    private DeviceRepository determineDeviceRepository(final DeviceType type) {
        return repositories.stream()
                .filter(repository -> repository.canHandle(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No repository found for device type: " + type));
    }

}
