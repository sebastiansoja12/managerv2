package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.vo.ReturnModel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.event.ShipmentStatusChangedEvent;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReturningEventListener {

    private final ReturningServicePort returningServicePort;

    public ReturningEventListener(final ReturningServicePort returningServicePort) {
        this.returningServicePort = returningServicePort;
    }

    @EventListener
    public void handle(final ShipmentStatusChangedEvent event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        if (snapshot.shipmentStatus() == ShipmentStatus.RETURN) {
            final Result<ReturnModel, ErrorCode> returnProcess = returningServicePort.notifyShipmentReturn(snapshot);
        }
    }
}
