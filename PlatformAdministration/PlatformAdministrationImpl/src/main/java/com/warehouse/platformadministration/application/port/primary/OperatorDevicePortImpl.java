package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.OperatorDeviceServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.platformadministration.domain.vo.OperatorDevice;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class OperatorDevicePortImpl implements OperatorDevicePort {

    static final Set<String> ALLOWED_STATUSES = Set.of("ACTIVE", "BLOCKED");

    private final OperatorServicePort operatorServicePort;

    private final OperatorDeviceServicePort operatorDeviceServicePort;

    private final OperatorUserServicePort operatorUserServicePort;

    public OperatorDevicePortImpl(final OperatorServicePort operatorServicePort,
                                  final OperatorDeviceServicePort operatorDeviceServicePort,
                                  final OperatorUserServicePort operatorUserServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorDeviceServicePort = operatorDeviceServicePort;
        this.operatorUserServicePort = operatorUserServicePort;
    }

    @Override
    public List<OperatorDevice> findDevices(final OperatorId operatorId) {
        return operatorDeviceServicePort.findAll(scope(operatorId));
    }

    @Override
    public void revokeDevice(final OperatorId operatorId, final String deviceId) {
        requireDeviceId(deviceId);
        if (!operatorDeviceServicePort.revoke(scope(operatorId), deviceId)) {
            throw new IllegalStateException("Device " + deviceId + " is not paired");
        }
        log.info("Platform owner revoked pairing of device {} for operator {}", deviceId, operatorId.value());
    }

    @Override
    public void changeDeviceStatus(final OperatorId operatorId, final String deviceId, final String status) {
        requireDeviceId(deviceId);
        if (status == null || !ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Device status must be one of " + ALLOWED_STATUSES);
        }
        operatorDeviceServicePort.changeStatus(scope(operatorId), deviceId, status);
        log.info("Platform owner changed device {} of operator {} to {}", deviceId, operatorId.value(), status);
    }

    @Override
    public List<OperatorApiKey> findApiKeys(final OperatorId operatorId) {
        return operatorUserServicePort.findApiKeys(scope(operatorId));
    }

    @Override
    public void revokeApiKey(final OperatorId operatorId, final UserId userId) {
        operatorUserServicePort.revokeApiKey(scope(operatorId), userId);
        log.info("Platform owner revoked API key of user {} for operator {}", userId.value(), operatorId.value());
    }

    private void requireDeviceId(final String deviceId) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Device identifier is required");
        }
    }

    private OperatorScope scope(final OperatorId operatorId) {
        return operatorServicePort.findScope(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
    }
}
