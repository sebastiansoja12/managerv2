package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.dto.UsernameDto;
import com.warehouse.terminal.dto.VersionDto;

public record TerminalAddRequestDto(DepartmentCodeDto departmentCode, UsernameDto username, VersionDto version, DeviceTypeDto deviceType) {
}
