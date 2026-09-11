package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record DevicePairingCodeCommand(UserId userId, OperatorId operatorId, DepartmentCode departmentCode) {
}
