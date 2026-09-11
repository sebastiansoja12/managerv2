package com.warehouse.mcp.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.terminal.dto.DeviceAccessDto;

public class DeviceServiceAdapter implements DeviceServicePort {

    private final DeviceApiService deviceApiService;

    public DeviceServiceAdapter(final DeviceApiService deviceApiService) {
        this.deviceApiService = deviceApiService;
    }

    @Override
    public List<DeviceAccess> findAllDevices() {
        return this.deviceApiService.findAllAccess().stream().map(this::map).toList();
    }

    private DeviceAccess map(final DeviceAccessDto device) {
        return new DeviceAccess(device.deviceId(), device.deviceType(), device.status(), device.departmentCode(),
                device.assignmentMode(), device.userId(), device.version(), device.paired(), device.lastSeenAt(),
                device.operatorUserId(), device.operatorSessionExpiresAt());
    }
}
