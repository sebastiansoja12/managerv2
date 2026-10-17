package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record OperatorDataExportResponse(String format,
                                         Instant exportedAt,
                                         String exportedBy,
                                         Operator operator,
                                         List<OperatorUserResponse> users,
                                         List<OperatorDepartmentResponse> departments,
                                         List<OperatorCourierResponse> couriers,
                                         List<OperatorDeviceResponse> devices,
                                         List<OperatorApiKeyResponse> apiKeys,
                                         Map<String, Object> shipmentSettings,
                                         List<PlatformAuditEntryResponse> auditLog) {

    public static final String FORMAT = "managerv2.operator-export.v1";

    public record Operator(Long operatorId,
                           String companyName,
                           String taxId,
                           String contactEmail,
                           String contactPhone,
                           LocalDate contractStartDate,
                           LocalDate contractEndDate,
                           LocalDate foundedDate,
                           String status,
                           Instant createdAt) {
    }
}
