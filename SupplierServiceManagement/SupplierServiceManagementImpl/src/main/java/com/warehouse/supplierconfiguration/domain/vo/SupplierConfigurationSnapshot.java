package com.warehouse.supplierconfiguration.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record SupplierConfigurationSnapshot(SupplierId supplierId,
                                            SupplierConfigurationValues values,
                                            Long version,
                                            Instant createdAt,
                                            Instant updatedAt,
                                            UserId updatedBy) {
}
