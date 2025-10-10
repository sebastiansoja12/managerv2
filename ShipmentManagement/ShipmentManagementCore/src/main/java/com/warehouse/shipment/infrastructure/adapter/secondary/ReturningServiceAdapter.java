package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ReturnModel;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReturningServiceAdapter implements ReturningServicePort {

    @Override
    public Result<ReturnModel, ErrorCode> notifyShipmentReturn(final ShipmentSnapshot snapshot) {
        log.info("Sending request to returning manager for shipment {}", snapshot.shipmentId().toString());
        return null;
    }
}
