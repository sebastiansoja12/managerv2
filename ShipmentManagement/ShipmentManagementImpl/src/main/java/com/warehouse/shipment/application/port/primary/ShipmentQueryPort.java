package com.warehouse.shipment.application.port.primary;

import com.warehouse.shipment.application.port.primary.result.ShipmentPageResult;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;

public interface ShipmentQueryPort {
    ShipmentPageResult searchShipments(final ShipmentSearchCriteria criteria);
}
