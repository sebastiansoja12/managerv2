package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.pickuppoint.domain.vo.PickupPointAuditLog;

public interface AuditLogServicePort {

    void record(final PickupPointAuditLog pickupPointAuditLog);
}
