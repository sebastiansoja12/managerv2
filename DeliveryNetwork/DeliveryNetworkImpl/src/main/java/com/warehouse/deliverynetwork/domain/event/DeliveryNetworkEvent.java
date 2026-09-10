package com.warehouse.deliverynetwork.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;

public interface DeliveryNetworkEvent extends DomainEvent {

    DeliveryNetworkSnapshot getSnapshot();
}
