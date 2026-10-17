package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.terminal.domain.model.DeviceVersion;
import com.warehouse.terminal.domain.port.secondary.DeviceVersionRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;

public class DeviceVersionRepositoryImpl implements DeviceVersionRepository {

    private static final String DEVICE_ID_FIELD = "deviceId.value";

    private final OperatorFilteredRepository<TerminalEntity> terminalRepository;

    private final OperatorFilteredRepository<MobileEntity> mobileRepository;

    private final OperatorFilteredRepository<ScannerEntity> scannerRepository;

    private final DeviceVersionReadRepository deviceVersionReadRepository;

    public DeviceVersionRepositoryImpl(final OperatorFilteredRepository<TerminalEntity> terminalRepository,
                                       final OperatorFilteredRepository<MobileEntity> mobileRepository,
                                       final OperatorFilteredRepository<ScannerEntity> scannerRepository,
                                       final DeviceVersionReadRepository deviceVersionReadRepository) {
        this.terminalRepository = terminalRepository;
        this.mobileRepository = mobileRepository;
        this.scannerRepository = scannerRepository;
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
        this.deviceVersionReadRepository.save(deviceVersionEntity);
    }

    private Optional<String> resolveCurrentVersion(final DeviceId deviceId) {
        final DeviceType type = deviceId.type();
        if (DeviceType.TERMINAL.equals(type)) {
            return findTerminal(deviceId)
                    .map(entity -> entity.getVersion() != null
                            ? entity.getVersion()
                            : (entity.getSoftware() != null ? entity.getSoftware().getAppVersion() : null));
        }
        if (DeviceType.SCANNER.equals(type)) {
            return findScanner(deviceId)
                    .map(entity -> (String) null);
        }
        if (DeviceType.MOBILE.equals(type)) {
            return findMobile(deviceId)
                    .map(entity -> entity.getVersion() != null
                            ? entity.getVersion()
                            : (entity.getSoftware() != null ? entity.getSoftware().getAppVersion() : null));
        }
        return Optional.empty();
    }

    private boolean deviceExists(final DeviceId deviceId) {
        final DeviceType type = deviceId.type();
        if (DeviceType.TERMINAL.equals(type)) {
            return findTerminal(deviceId).isPresent();
        }
        if (DeviceType.SCANNER.equals(type)) {
            return findScanner(deviceId).isPresent();
        }
        if (DeviceType.MOBILE.equals(type)) {
            return findMobile(deviceId).isPresent();
        }
        return false;
    }

    private Optional<TerminalEntity> findTerminal(final DeviceId deviceId) {
        return terminalRepository.createCriteria(TerminalEntity.class)
                .eq(DEVICE_ID_FIELD, deviceId.value())
                .one();
    }

    private Optional<ScannerEntity> findScanner(final DeviceId deviceId) {
        return scannerRepository.createCriteria(ScannerEntity.class)
                .eq(DEVICE_ID_FIELD, deviceId.value())
                .one();
    }

    private Optional<MobileEntity> findMobile(final DeviceId deviceId) {
        return mobileRepository.createCriteria(MobileEntity.class)
                .eq(DEVICE_ID_FIELD, deviceId.value())
                .one();
    }
}
