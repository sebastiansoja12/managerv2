package com.warehouse.deliverynetwork.domain.event;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;

public interface DeliveryNetworkEvent extends DomainEvent {

    DeliveryNetworkSnapshot getSnapshot();
}
