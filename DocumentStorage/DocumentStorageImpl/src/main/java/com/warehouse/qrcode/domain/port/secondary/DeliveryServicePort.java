package com.warehouse.qrcode.domain.port.secondary;

import com.warehouse.qrcode.domain.vo.ShipmentId;

public interface DeliveryServicePort {
    String findDestinationDepartmentCode(final ShipmentId shipmentId);
}
