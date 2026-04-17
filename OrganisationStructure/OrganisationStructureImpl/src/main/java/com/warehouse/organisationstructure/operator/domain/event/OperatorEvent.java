package com.warehouse.organisationstructure.operator.domain.event;

import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

import java.time.Instant;

public interface OperatorEvent {

    OperatorSnapshot getSnapshot();

    Instant getTimestamp();
}
