package com.warehouse.terminal.request;

import java.time.Instant;

import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.UserIdDto;

public record DeviceOwnershipRequestDto(
        UserIdDto userId,
        UserIdDto previousUserId,
        DepartmentCodeDto departmentCode,
        Long teamId,
        Long vehicleId,
        String assignedRole,
        Instant assignedAt,
        Instant unassignedAt
) {
}
