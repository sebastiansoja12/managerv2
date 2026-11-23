package com.warehouse.shipment.domain.event;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentEvent extends DomainEvent {

    ShipmentSnapshot getSnapshot();
}
