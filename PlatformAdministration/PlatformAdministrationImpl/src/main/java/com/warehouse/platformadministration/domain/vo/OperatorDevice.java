package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record OperatorDevice(String deviceId,
                             String deviceType,
                             String status,
                             UserId userId,
                             DepartmentCode departmentCode,
                             String version,
                             Instant lastUpdate,
                             boolean paired,
                             Instant pairKeyExpiresAt,
                             Instant lastLoginAt) {
}
