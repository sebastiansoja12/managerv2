package com.warehouse.organisationstructure.operator.domain.event;

import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

import java.time.Instant;

public class OperatorCreatedEvent extends OperatorChangedEvent implements OperatorEvent {

    public OperatorCreatedEvent(final OperatorSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
