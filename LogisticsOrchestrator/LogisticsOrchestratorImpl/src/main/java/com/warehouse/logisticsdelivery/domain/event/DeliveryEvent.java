package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

public interface DeliveryEvent extends DomainEvent {

    DeliverySnapshot getSnapshot();
}
