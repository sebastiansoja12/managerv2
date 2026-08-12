package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.kafka.domain.model.KafkaEventKey;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;

public interface ShipmentEvent extends KafkaEventKey, OperatorAwareEvent {
}
