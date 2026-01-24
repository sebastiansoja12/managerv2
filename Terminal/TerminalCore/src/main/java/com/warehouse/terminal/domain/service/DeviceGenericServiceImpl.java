package com.warehouse.terminal.domain.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.context.DomainContext;
import com.warehouse.terminal.domain.event.DeviceCreated;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.device.Device;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.port.secondary.DeviceGenericRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceSettingsRepository;

public class DeviceGenericServiceImpl implements DeviceGenericService {

    private final DeviceGenericRepository deviceRepository;

    private final DeviceSettingsRepository deviceSettingsRepository;

	public DeviceGenericServiceImpl(final DeviceGenericRepository deviceRepository,
                                    DeviceSettingsRepository deviceSettingsRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceSettingsRepository = deviceSettingsRepository;
    }

    @Override
    public void create(final Device device) {
        this.deviceRepository.create(device);
        DomainContext.eventPublisher().publishEvent(new DeviceCreated(
               null, Instant.now()
        ));
    }

    @Override
    public void changeDeviceType(final DeviceId deviceId, final DeviceType deviceType) {
        final Device device = this.deviceRepository.findById(deviceId);
        device.updateDeviceType(deviceType);
        this.deviceRepository.create(device);
    }

    @Override
    public void assignUser(final DeviceId deviceId, final UserId userId) {
        final Device device = this.deviceRepository.findById(deviceId);
        device.assignUser(userId);
        this.deviceRepository.create(device);
    }

    @Override
    public void updateVersion(final DeviceId deviceId, final String version) {
        final Terminal terminal = (Terminal) this.deviceRepository.findById(deviceId);
        terminal.updateVersion(version);
        this.deviceRepository.create(terminal);
    }

    @Override
    public Terminal findByDeviceId(final DeviceId deviceId) {
        return (Terminal) this.deviceRepository.findById(deviceId);
    }

    @Override
    public List findAll() {
        return Collections.emptyList();
    }

    @Override
    public void updateSettings(final DeviceSettingsRequest request) {
        final DeviceSettings deviceSettings = DeviceSettings.from(request);
        this.deviceSettingsRepository.saveOrUpdate(deviceSettings);
    }

    @Override
    public DeviceId nextDeviceId(final DeviceType deviceType) {
        return this.deviceRepository.nextDeviceId(deviceType);
    }

}
