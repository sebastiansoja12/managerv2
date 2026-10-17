package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import java.util.List;

public interface ShipmentQueryPort {
    List<ShipmentResult> searchShipments(final ShipmentSearchCriteria criteria);
}
