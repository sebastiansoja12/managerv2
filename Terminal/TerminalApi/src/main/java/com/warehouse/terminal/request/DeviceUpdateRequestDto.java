package com.warehouse.terminal.request;

import java.time.Instant;

import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.dto.UserIdDto;
import com.warehouse.terminal.dto.VersionDto;

public record DeviceUpdateRequestDto(
        DeviceIdDto deviceId,
        String externalDeviceId,
        DeviceTypeDto deviceType,
        String status,
        UserIdDto userId,
        DepartmentCodeDto departmentCode,
        VersionDto version,
        Instant createdAt,
        Instant updatedAt,
        Instant lastUpdate,
        Boolean active,
        DeviceIdentityRequestDto identity,
        DeviceHardwareRequestDto hardware,
        DeviceSoftwareRequestDto software,
        DeviceNetworkRequestDto network,
        DeviceSecurityRequestDto security,
        DeviceLocationRequestDto location,
        DeviceOwnershipRequestDto ownership
) {
}
