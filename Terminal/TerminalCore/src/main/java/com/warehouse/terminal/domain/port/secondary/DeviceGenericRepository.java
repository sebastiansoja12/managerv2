package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.device.Device;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class DeviceGenericRepository {

    private final Set<DeviceRepository> repositories;

    public DeviceGenericRepository(final Set<DeviceRepository> repositories) {
        this.repositories = repositories;
    }

    public Device findById(final DeviceId deviceId) {
        final DeviceRepository repository = determineDeviceRepository(deviceId.type());
        return repository.findById(deviceId);
    }

    public void create(final Device device) {
        final DeviceRepository repository = determineDeviceRepository(device.getDeviceType());
        repository.saveOrUpdate(device);
    }

    public DeviceId nextDeviceId(final DeviceType type) {
        final DeviceRepository repository = determineDeviceRepository(type);
        return repository.nextDeviceId();
    }

    private DeviceRepository determineDeviceRepository(final DeviceType type) {
        return repositories.stream()
                .filter(repository -> repository.canHandle(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No repository found for device type: " + type));
    }

}
