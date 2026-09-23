package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.domain.vo.DeliveryAuditLog;

public interface AuditLogServicePort {

    void record(final DeliveryAuditLog deliveryAuditLog);
}
