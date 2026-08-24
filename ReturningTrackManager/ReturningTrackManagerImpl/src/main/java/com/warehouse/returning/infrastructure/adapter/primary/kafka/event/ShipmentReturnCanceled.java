package com.warehouse.returning.infrastructure.adapter.primary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.returning.domain.vo.UserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentReturnCanceled extends ShipmentEvent {

    @JsonCreator
    public ShipmentReturnCanceled(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                                  @JsonProperty("timestamp") final Instant timestamp,
                                  @JsonProperty("userId") final UserId userId,
                                  @JsonProperty("operatorId") final OperatorId operatorId) {
        super(snapshot, timestamp, userId, operatorId);
    }
}
