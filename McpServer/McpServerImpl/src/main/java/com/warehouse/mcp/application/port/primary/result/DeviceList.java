package com.warehouse.mcp.application.port.primary.result;

import java.util.List;

import com.warehouse.mcp.domain.vo.DeviceAccess;

public record DeviceList(int totalDevices, List<DeviceAccess> devices) {
}
