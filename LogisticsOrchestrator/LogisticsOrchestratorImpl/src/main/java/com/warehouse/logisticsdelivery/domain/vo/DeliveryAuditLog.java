package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryAuditLogType;

import java.util.List;
import java.util.Objects;

public record DeliveryAuditLog(DeliveryId deliveryId,
                               DeliveryAuditLogType type,
                               List<FieldDifference> changes) {

    public DeliveryAuditLog {
        Objects.requireNonNull(deliveryId, "Delivery ID cannot be null");
        Objects.requireNonNull(type, "Delivery audit log type cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Delivery changes cannot be null"));
    }
}
