package com.warehouse.department.infrastructure.adapter.primary.api.dto;

import java.time.Instant;

public record DepartmentApi(DepartmentCodeApi departmentCode,
                            AddressApi address,
                            String nip,
                            String telephoneNumber,
                            String openingHours,
                            Boolean active,
                            String departmentType,
                            Instant createdAt,
                            Instant updatedAt) {
}
