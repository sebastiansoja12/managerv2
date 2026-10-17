package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.infrastructure.GeocodingApiService;
import com.warehouse.platformadministration.application.port.secondary.OperatorGeocodingServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

public class OperatorGeocodingServiceAdapter implements OperatorGeocodingServicePort {

    private final GeocodingApiService geocodingApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorGeocodingServiceAdapter(final GeocodingApiService geocodingApiService,
                                           final OperatorScopeExecutor operatorScopeExecutor) {
        this.geocodingApiService = geocodingApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public boolean hasEnabledConfiguration(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, geocodingApiService::hasEnabledConfiguration);
    }
}
