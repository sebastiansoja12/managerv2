package com.warehouse.shipment.application.port.secondary;

import com.warehouse.shipment.domain.vo.ShipmentAuditLog;

public interface AuditLogServicePort {

    void record(final ShipmentAuditLog shipmentAuditLog);
}
