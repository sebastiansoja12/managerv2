package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import org.springframework.beans.factory.ObjectProvider;

public class OperatorStatusServiceAdapter implements OperatorStatusServicePort {

    private final ObjectProvider<OperatorApiService> operatorApiService;

    private final OperatorAccessCache operatorAccessCache;

    public OperatorStatusServiceAdapter(final ObjectProvider<OperatorApiService> operatorApiService,
                                        final OperatorAccessCache operatorAccessCache) {
        this.operatorApiService = operatorApiService;
        this.operatorAccessCache = operatorAccessCache;
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        return operatorAccessCache.findOperatorActive(operatorId)
                .orElseGet(() -> {
                    final boolean active = operatorApiService.getObject().isActive(operatorId);
                    operatorAccessCache.putOperatorActive(operatorId, active);
                    return active;
                });
    }
}
