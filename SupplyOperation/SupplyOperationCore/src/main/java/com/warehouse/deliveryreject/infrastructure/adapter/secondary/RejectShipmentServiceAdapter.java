package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RejectShipmentServiceAdapter implements RejectShipmentServicePort {
    @Override
    public ShipmentRejectResponse notifyShipmentRejection(final ShipmentRejectRequest request) {
        return null;
    }
}
