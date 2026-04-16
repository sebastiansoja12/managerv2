package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DepartmentCodeDto;

public record CurrentUserDevicePairRequestDto(String externalSystemId, DepartmentCodeDto departmentCode) {
}
