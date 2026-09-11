package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record DeviceListToolResponse(int totalDevices, int returnedDevices, List<DeviceToolResponse> devices) {
}
