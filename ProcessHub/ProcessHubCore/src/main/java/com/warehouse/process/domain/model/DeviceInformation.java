package com.warehouse.process.domain.model;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

public record DeviceInformation(DeviceId deviceId,
                                DepartmentCode departmentCode,
                                UserId userId,
                                DeviceType deviceType,
                                DeviceUserType deviceUserType,
                                String version) {
}
