package com.warehouse.mcp.domain.vo;

public record DeviceFilter(String status, String departmentCode) {

    public boolean matches(final DeviceAccess device) {
        return (status == null || status.isBlank() || status.equalsIgnoreCase(device.status()))
                && (departmentCode == null || departmentCode.isBlank()
                || departmentCode.equalsIgnoreCase(device.departmentCode()));
    }
}
