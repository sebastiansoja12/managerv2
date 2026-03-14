package com.warehouse.terminal.domain.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.context.DomainContext;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.event.*;
import com.warehouse.terminal.domain.exception.DeviceNotFoundException;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceSettings;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.port.secondary.DeviceGenericRepository;
import com.warehouse.terminal.domain.port.secondary.DeviceSettingsRepository;
import com.warehouse.terminal.domain.vo.*;

public class DeviceGenericServiceImpl implements DeviceGenericService {

    private final DeviceGenericRepository deviceRepository;
    private final DeviceSettingsRepository deviceSettingsRepository;

    public DeviceGenericServiceImpl(final DeviceGenericRepository deviceRepository,
                                    final DeviceSettingsRepository deviceSettingsRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceSettingsRepository = deviceSettingsRepository;
    }

    @Override
    public void create(final Device device) {
        this.deviceRepository.create(device);
        DomainContext.publishAfterCommit(new DeviceCreated(device.toSnapshot(), Instant.now()));
    }

    @Override
    public void assignUser(final DeviceId deviceId, final UserId userId) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.assignUser(userId);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceUserChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateVersion(final DeviceId deviceId, final String version) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.changeVersion(version);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceVersionChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateDevice(final DeviceUpdateCommand request) {
        this.deviceRepository.findById(request.deviceId()).ifPresent(device -> {
            device.update(request);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceUpdated(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateDeviceType(final DeviceId deviceId, final DeviceType deviceType) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateDeviceType(deviceType);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceTypeChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateActive(final DeviceId deviceId, final Boolean active) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateActive(active);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceActiveChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateStatus(final DeviceId deviceId, final DeviceStatus status) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateStatus(status);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceStatusChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateIdentity(final DeviceId deviceId, final DeviceIdentity identity) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateIdentity(identity);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceIdentityChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateHardware(final DeviceId deviceId, final DeviceHardware hardware) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateHardware(hardware);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceHardwareChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateSoftware(final DeviceId deviceId, final DeviceSoftware software) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateSoftware(software);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceSoftwareChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateNetwork(final DeviceId deviceId, final DeviceNetwork network) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateNetwork(network);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceNetworkChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateSecurity(final DeviceId deviceId, final SecurityProfile security) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateSecurity(security);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceSecurityChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateLocation(final DeviceId deviceId, final DeviceLocation location) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateLocation(location);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceLocationChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateOwnership(final DeviceId deviceId, final OwnershipProfile ownership) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateOwnership(ownership);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceOwnershipChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public void updateVersionField(final DeviceId deviceId, final String version) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            device.updateVersion(version);
            this.deviceRepository.update(device);
            DomainContext.publishAfterCommit(new DeviceVersionFieldChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public Device findByDeviceId(final DeviceId deviceId) {
        return this.deviceRepository.findById(deviceId)
                .orElseThrow(() -> DeviceNotFoundException.forDeviceId(deviceId));
    }

    @Override
    public List<Device> findAll() {
        return this.deviceRepository.findAll();
    }

    @Override
    public void updateSettings(final DeviceId deviceId, final DeviceSettings deviceSettings) {
        this.deviceRepository.findById(deviceId).ifPresent(device -> {
            this.deviceSettingsRepository.saveOrUpdate(deviceSettings);
            DomainContext.publishAfterCommit(new DeviceSettingsChanged(device.toSnapshot(), Instant.now()));
        });
    }

    @Override
    public DeviceId nextDeviceId(final DeviceType deviceType) {
        return this.deviceRepository.nextDeviceId(deviceType);
    }

    @Override
    public Optional<Device> findByExternalSystemId(final String externalSystemId) {
        return this.deviceRepository.findByExternalSystemId(externalSystemId);
    }

    @Override
    public List<Device> findByUserId(final UserId userId) {
        return this.deviceRepository.findByUserId(userId);
    }
}
