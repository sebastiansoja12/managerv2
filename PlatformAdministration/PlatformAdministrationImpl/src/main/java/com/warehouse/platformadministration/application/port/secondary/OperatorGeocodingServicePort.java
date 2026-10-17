package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorScope;

public interface OperatorGeocodingServicePort {

    boolean hasEnabledConfiguration(final OperatorScope operatorScope);
}
