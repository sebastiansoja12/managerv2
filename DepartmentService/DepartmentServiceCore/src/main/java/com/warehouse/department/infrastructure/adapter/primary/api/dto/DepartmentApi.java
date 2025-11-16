package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import java.time.Instant;

public record DepartmentApi(DepartmentCodeApi departmentCode,
                            AddressApi address,
                            String taxId,
                            String telephoneNumber,
                            String openingHours,
                            String email,
                            String departmentType,
                            String status,
                            Instant createdAt,
                            Instant updatedAt) {
}
