package com.warehouse.shipment.domain.vo.kafka;

import com.warehouse.commonassets.kafkapublisher.KeyedKafkaMessage;

public record ShipmentCreatedMessage(Long shipmentId) implements KeyedKafkaMessage {
    @Override
    public String kafkaKey() {
        return "shipment.created";
    }
}
