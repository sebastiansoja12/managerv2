package com.warehouse.shipment.domain.vo.kafka;

import com.warehouse.commonassets.kafkapublisher.KeyedKafkaMessage;

public record ShipmentReturnMessage(Long shipmentId,
                                    String reason,
                                    String reasonCode,
                                    String departmentCode,
                                    Long userId) implements KeyedKafkaMessage {

    @Override
    public String kafkaKey() {
        return "shipment.return.created";
    }
}
