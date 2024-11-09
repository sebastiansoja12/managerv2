package com.warehouse.terminal.request;

import com.warehouse.terminal.dto.DepotCodeDto;
import com.warehouse.terminal.dto.DeviceTypeDto;
import com.warehouse.terminal.dto.UsernameDto;
import com.warehouse.terminal.dto.VersionDto;

public record TerminalAddRequestDto(DepotCodeDto depotCode, UsernameDto username, VersionDto version, DeviceTypeDto deviceType) {
}
