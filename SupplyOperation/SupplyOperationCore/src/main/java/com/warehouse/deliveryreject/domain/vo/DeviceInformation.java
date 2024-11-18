package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceInformation(DeviceId deviceId, DepartmentCode departmentCode,
                                String version, String username) {
}
