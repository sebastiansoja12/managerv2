package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record DeviceToolResponse(String deviceId,
                                 String deviceType,
                                 String status,
                                 String departmentCode,
                                 String assignmentMode,
                                 Long ownerUserId,
                                 String appVersion,
                                 boolean paired,
                                 String lastSeenAt,
                                 Long operatorUserId,
                                 String operatorSessionExpiresAt) {
}
