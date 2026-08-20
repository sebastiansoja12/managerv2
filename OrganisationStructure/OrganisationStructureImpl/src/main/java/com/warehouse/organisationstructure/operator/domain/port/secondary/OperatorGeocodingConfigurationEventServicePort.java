package com.warehouse.organisationstructure.operator.domain.port.secondary;

import java.time.Instant;

import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

public interface OperatorGeocodingConfigurationEventServicePort {
    void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp);
}
