package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkAuditLog;

public interface AuditLogServicePort {

    void record(final DeliveryNetworkAuditLog deliveryNetworkAuditLog);
}
