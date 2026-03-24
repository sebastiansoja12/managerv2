package com.warehouse.terminal.domain.service;

import java.time.Instant;
import java.util.List;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.context.DomainContext;
import com.warehouse.terminal.domain.event.*;
import com.warehouse.terminal.domain.exception.DeviceFieldUpdateNotSupportedException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.command.DeviceSettingsRequest;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
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
        DomainContext.publishAfterCommit(new DeviceCreated(device.toSnapshot(), Instant.now()));
    }

    @Override
    public void changeDeviceType(final DeviceId deviceId, final DeviceType deviceType) {
        final Device device = this.deviceRepository.findById(deviceId);
        device.updateDeviceType(deviceType);
        this.deviceRepository.create(device);
        DomainContext.publishAfterCommit(new DeviceTypeChanged(device.toSnapshot(), Instant.now()));
    }

    @Override
    public void assignUser(final DeviceId deviceId, final UserId userId) {
        final Device device = this.deviceRepository.findById(deviceId);
        device.assignUser(userId);
        this.deviceRepository.create(device);
        DomainContext.publishAfterCommit(new DeviceUserChanged(device.toSnapshot(), Instant.now()));
    }

    @Override
    public void updateVersion(final DeviceId deviceId, final String version) {
        final Device device = this.deviceRepository.findById(deviceId);
        device.changeVersion(version);
        this.deviceRepository.create(device);
        DomainContext.publishAfterCommit(new DeviceVersionChanged(device.toSnapshot(), Instant.now()));
    }

    @Override
    public void updateDevice(final DeviceUpdateCommand request) {
        final Device device = this.deviceRepository.findById(request.deviceId());
        applyUpdates(device, request);
        this.deviceRepository.create(device);
        DomainContext.publishAfterCommit(new DeviceChanged(device.toSnapshot(), Instant.now()));
    }

    @Override
    public Device findByDeviceId(final DeviceId deviceId) {
        return this.deviceRepository.findById(deviceId);
    }

    @Override
    public List<Device> findAll() {
        return this.deviceRepository.findAll();
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

    private void applyUpdates(final Device device, final DeviceUpdateCommand request) {
        final DeviceId deviceId = request.deviceId();

        updateField(device, deviceId, "externalDeviceId", request.externalDeviceId(), () -> device.updateExternalDeviceId(request.externalDeviceId()));
        updateField(device, deviceId, "deviceType", request.deviceType(), () -> device.updateDeviceType(request.deviceType()));
        updateField(device, deviceId, "active", request.active(), () -> device.updateActive(request.active()));
        updateField(device, deviceId, "status", request.status(), () -> device.updateStatus(request.status()));
        updateField(device, deviceId, "identity", request.identity(), () -> device.updateIdentity(request.identity()));
        updateField(device, deviceId, "hardware", request.hardware(), () -> device.updateHardware(request.hardware()));
        updateField(device, deviceId, "software", request.software(), () -> device.updateSoftware(request.software()));
        updateField(device, deviceId, "network", request.network(), () -> device.updateNetwork(request.network()));
        updateField(device, deviceId, "security", request.security(), () -> device.updateSecurity(request.security()));
        updateField(device, deviceId, "location", request.location(), () -> device.updateLocation(request.location()));
        updateField(device, deviceId, "ownership", request.ownership(), () -> device.updateOwnership(request.ownership()));
        updateField(device, deviceId, "userId", request.userId(), () -> device.updateUserId(request.userId()));
        updateField(device, deviceId, "departmentCode", request.departmentCode(), () -> device.updateDepartmentCode(request.departmentCode()));
        updateField(device, deviceId, "version", request.version(), () -> device.updateVersion(request.version()));
        updateField(device, deviceId, "createdAt", request.createdAt(), () -> device.updateCreatedAt(request.createdAt()));
        updateField(device, deviceId, "updatedAt", request.updatedAt(), () -> device.updateUpdatedAt(request.updatedAt()));
        updateField(device, deviceId, "lastUpdate", request.lastUpdate(), () -> device.updateLastUpdate(request.lastUpdate()));
    }

    private void updateField(final Device device,
                             final DeviceId deviceId,
                             final String fieldName,
                             final Object value,
                             final Runnable updateOperation) {
        if (value != null) {
            try {
                updateOperation.run();
            } catch (final UnsupportedOperationException ex) {
                throw DeviceFieldUpdateNotSupportedException.forField(deviceId, device.getDeviceType(), fieldName);
            }
        }
    }

}
