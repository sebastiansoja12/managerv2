package com.warehouse.supplierconfiguration.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierConfigurationChangeType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SupplierConfigurationRevision(UUID revisionId,
                                            SupplierId supplierId,
                                            SupplierConfigurationChangeType changeType,
                                            UserId changedBy,
                                            Instant changedAt,
                                            List<SupplierConfigurationChange> changes) {

    public SupplierConfigurationRevision {
        changes = List.copyOf(changes);
    }
}
