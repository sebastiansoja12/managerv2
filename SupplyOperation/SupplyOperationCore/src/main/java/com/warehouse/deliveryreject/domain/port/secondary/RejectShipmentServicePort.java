package com.warehouse.deliveryreject.domain.port.secondary;

import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;

public interface RejectShipmentServicePort {
    ShipmentRejectResponse notifyShipmentRejection(final ShipmentRejectRequest request);
}
