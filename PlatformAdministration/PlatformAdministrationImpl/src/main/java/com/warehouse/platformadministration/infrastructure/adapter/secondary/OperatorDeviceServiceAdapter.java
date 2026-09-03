package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.dto.DeviceAccessDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.platformadministration.application.port.secondary.OperatorDeviceServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorDevice;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.List;

public class OperatorDeviceServiceAdapter implements OperatorDeviceServicePort {

    private final DeviceApiService deviceApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorDeviceServiceAdapter(final DeviceApiService deviceApiService,
                                        final OperatorScopeExecutor operatorScopeExecutor) {
        this.deviceApiService = deviceApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public List<OperatorDevice> findAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, () -> deviceApiService.findAllAccess()
                .stream()
                .map(OperatorDeviceServiceAdapter::toDevice)
                .toList());
    }

    @Override
    public boolean revoke(final OperatorScope operatorScope, final String deviceId) {
        return operatorScopeExecutor.execute(operatorScope,
                () -> deviceApiService.revokeAccess(new DeviceIdDto(deviceId)));
    }

    @Override
    public void changeStatus(final OperatorScope operatorScope, final String deviceId, final String status) {
        operatorScopeExecutor.execute(operatorScope, () -> {
            deviceApiService.changeStatus(new DeviceIdDto(deviceId), status);
            return null;
        });
    }

    @Override
    public int retireAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, deviceApiService::retireAll);
    }

    static OperatorDevice toDevice(final DeviceAccessDto device) {
        return new OperatorDevice(device.deviceId(), device.deviceType(), device.status(),
                device.userId() != null ? new UserId(device.userId()) : null,
                device.departmentCode() != null ? new DepartmentCode(device.departmentCode()) : null,
                device.version(), device.lastUpdate(), device.paired(), device.pairKeyExpiresAt(),
                device.lastLoginAt());
    }
}
