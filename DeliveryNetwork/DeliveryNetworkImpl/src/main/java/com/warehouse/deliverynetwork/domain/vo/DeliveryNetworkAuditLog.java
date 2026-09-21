package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.enumeration.DeliveryNetworkAuditLogType;

import java.util.List;
import java.util.Objects;

public record DeliveryNetworkAuditLog(OperatorId operatorId,
                                      DeliveryNetworkAuditLogType type,
                                      List<FieldDifference> changes) {

    public DeliveryNetworkAuditLog {
        Objects.requireNonNull(operatorId, "Operator ID cannot be null");
        Objects.requireNonNull(type, "Delivery network audit log type cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Delivery network changes cannot be null"));
    }
}
