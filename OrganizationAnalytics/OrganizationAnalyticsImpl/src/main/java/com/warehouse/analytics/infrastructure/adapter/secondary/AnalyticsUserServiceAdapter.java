package com.warehouse.analytics.infrastructure.adapter.secondary;

import com.warehouse.analytics.application.port.secondary.AnalyticsUserServicePort;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;

public class AnalyticsUserServiceAdapter implements AnalyticsUserServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public AnalyticsUserServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public UserId currentUserId() {
        return this.operatorContextProvider.currentUserId();
    }
}
