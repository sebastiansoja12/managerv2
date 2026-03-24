package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

public class DeviceVersionRepositoryImpl implements DeviceVersionRepository {

    private final TerminalReadRepository terminalReadRepository;

    private final MobileReadRepository mobileReadRepository;

    private final ScannerReadRepository scannerReadRepository;

    private final DeviceVersionReadRepository deviceVersionReadRepository;

    public DeviceVersionRepositoryImpl(final TerminalReadRepository terminalReadRepository,
                                       final MobileReadRepository mobileReadRepository,
                                       final ScannerReadRepository scannerReadRepository,
                                       final DeviceVersionReadRepository deviceVersionReadRepository) {
        this.terminalReadRepository = terminalReadRepository;
        this.mobileReadRepository = mobileReadRepository;
        this.scannerReadRepository = scannerReadRepository;
        this.deviceVersionReadRepository = deviceVersionReadRepository;
    }

    @Override
    public boolean updateRequired(final DeviceId deviceId) {
        final Optional<String> currentVersionOptional = resolveCurrentVersion(deviceId);
        if (currentVersionOptional.isEmpty()) {
            if (deviceExists(deviceId)) {
                return true;
            }
            throw new RuntimeException("Device not found");
        }
        final String currentVersion = currentVersionOptional.get();

        final DeviceVersionEntity deviceVersionEntity = this.deviceVersionReadRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Device version not found"));

        if (currentVersion == null || deviceVersionEntity.getVersion() == null) {
            return true;
        }
        return !deviceVersionEntity.getVersion().equals(currentVersion);
    }

    @Override
    public Optional<DeviceVersion> find(final DeviceId deviceId) {
        return this.deviceVersionReadRepository
                .findByDeviceId(deviceId)
                .map(DeviceVersion::from);
    }

    @Override
    public void saveOrUpdate(final DeviceVersion deviceVersion) {
        final DeviceVersionEntity deviceVersionEntity = DeviceVersionEntity.from(deviceVersion);
        this.deviceVersionReadRepository.saveAndFlush(deviceVersionEntity);
    }

    private Optional<String> resolveCurrentVersion(final DeviceId deviceId) {
        final DeviceType type = deviceId.type();
        if (DeviceType.TERMINAL.equals(type)) {
            return terminalReadRepository.findById(deviceId)
                    .map(entity -> entity.getVersion() != null
                            ? entity.getVersion()
                            : (entity.getSoftware() != null ? entity.getSoftware().getAppVersion() : null));
        }
        if (DeviceType.SCANNER.equals(type)) {
            return scannerReadRepository.findById(deviceId)
                    .map(entity -> (String) null);
        }
        if (DeviceType.MOBILE.equals(type)) {
            return mobileReadRepository.findById(deviceId)
                    .map(entity -> entity.getVersion() != null
                            ? entity.getVersion()
                            : (entity.getSoftware() != null ? entity.getSoftware().getAppVersion() : null));
        }
        return Optional.empty();
    }

    private boolean deviceExists(final DeviceId deviceId) {
        final DeviceType type = deviceId.type();
        if (DeviceType.TERMINAL.equals(type)) {
            return terminalReadRepository.existsById(deviceId);
        }
        if (DeviceType.SCANNER.equals(type)) {
            return scannerReadRepository.existsById(deviceId);
        }
        if (DeviceType.MOBILE.equals(type)) {
            return mobileReadRepository.existsById(deviceId);
        }
        return false;
    }
}
