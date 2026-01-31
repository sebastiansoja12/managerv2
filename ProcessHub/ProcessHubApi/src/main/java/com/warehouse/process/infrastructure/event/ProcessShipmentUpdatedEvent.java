package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;
import com.warehouse.process.infrastructure.dto.ShipmentUpdateDto;

public class ProcessShipmentUpdatedEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final ShipmentUpdateDto shipmentUpdate;

    public ProcessShipmentUpdatedEvent(final ProcessLogId processLogId,
                                       final ServiceType serviceType,
                                       final LocalDateTime createdAt,
                                       final ShipmentUpdateDto shipmentUpdate) {
        super(processLogId, serviceType, createdAt);
        this.shipmentUpdate = shipmentUpdate;
    }

    public ShipmentUpdateDto getShipmentUpdate() {
        return shipmentUpdate;
    }
}
