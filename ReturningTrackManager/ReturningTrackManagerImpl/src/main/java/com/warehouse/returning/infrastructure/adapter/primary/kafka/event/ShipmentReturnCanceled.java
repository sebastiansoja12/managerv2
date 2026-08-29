package com.warehouse.returning.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentReturnCanceled extends ShipmentEvent {

    private ShipmentId shipmentId;

    @JsonCreator
    public ShipmentReturnCanceled(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                                  @JsonProperty("timestamp") final Instant timestamp,
                                  @JsonProperty("userId") final UserId userId,
                                  @JsonProperty("operatorId") final OperatorId operatorId,
                                  @JsonProperty("shipmentId") final ShipmentId shipmentId) {
        super(snapshot, timestamp, userId, operatorId);
        this.shipmentId = shipmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}
