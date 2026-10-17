package com.warehouse.platformadministration.domain.model;

import com.warehouse.platformadministration.domain.vo.OperatorApiKey;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorDevice;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import com.warehouse.platformadministration.domain.vo.OperatorUser;

import java.time.Instant;
import java.util.List;

public record OperatorDataExport(Instant exportedAt,
                                 String exportedBy,
                                 OperatorProfile operator,
                                 List<OperatorUser> users,
                                 List<OperatorDepartment> departments,
                                 List<OperatorCourier> couriers,
                                 List<OperatorDevice> devices,
                                 List<OperatorApiKey> apiKeys,
                                 OperatorShipmentSettings shipmentSettings,
                                 List<PlatformAuditEntry> auditLog) {
}
