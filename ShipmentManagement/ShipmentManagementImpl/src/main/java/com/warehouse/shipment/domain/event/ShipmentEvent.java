package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentEvent extends DomainEvent {

    ShipmentSnapshot getSnapshot();
}
