package com.warehouse.deliveryreturn.domain.port.secondary;

import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusShipmentRequest;

public interface ShipmentStatusControlServicePort {
    UpdateStatus updateStatus(final UpdateStatusShipmentRequest updateStatusShipmentRequest);
}
