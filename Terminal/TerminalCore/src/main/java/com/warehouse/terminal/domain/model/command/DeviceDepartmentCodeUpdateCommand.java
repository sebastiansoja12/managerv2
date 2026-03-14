package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceDepartmentCodeUpdateCommand(
        DeviceId deviceId,
        DepartmentCode departmentCode
) {
}
