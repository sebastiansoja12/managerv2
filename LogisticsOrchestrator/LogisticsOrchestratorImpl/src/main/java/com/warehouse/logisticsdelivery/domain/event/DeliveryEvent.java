package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

public interface DeliveryEvent extends DomainEvent {

    DeliverySnapshot getSnapshot();
}
