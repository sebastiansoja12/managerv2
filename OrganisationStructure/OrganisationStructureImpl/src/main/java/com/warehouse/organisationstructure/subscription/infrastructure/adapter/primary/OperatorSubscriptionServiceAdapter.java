package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.api.OperatorSubscriptionApiService;
import com.warehouse.organisationstructure.subscription.application.port.primary.OperatorSubscriptionPort;

public class OperatorSubscriptionServiceAdapter implements OperatorSubscriptionApiService {

    private final OperatorSubscriptionPort operatorSubscriptionPort;

    public OperatorSubscriptionServiceAdapter(final OperatorSubscriptionPort operatorSubscriptionPort) {
        this.operatorSubscriptionPort = operatorSubscriptionPort;
    }

    @Override
    public boolean isCurrentAccessAllowed() {
        return operatorSubscriptionPort.isCurrentAccessAllowed();
    }
}
