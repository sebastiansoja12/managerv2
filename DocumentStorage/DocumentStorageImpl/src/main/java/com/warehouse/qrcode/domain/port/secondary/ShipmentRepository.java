package com.warehouse.qrcode.domain.port.secondary;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.vo.ShipmentId;

public interface ShipmentRepository {
    Shipment find(final ShipmentId id);
}
