package com.warehouse.pickuppoint.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

public interface PickupPointEvent extends DomainEvent {

    PickupPointSnapshot getSnapshot();
}
