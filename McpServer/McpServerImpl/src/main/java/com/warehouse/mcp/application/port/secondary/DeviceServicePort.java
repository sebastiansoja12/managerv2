package com.warehouse.mcp.application.port.secondary;

import java.util.List;

import com.warehouse.mcp.domain.vo.DeviceAccess;

public interface DeviceServicePort {
    List<DeviceAccess> findAllDevices();
}
