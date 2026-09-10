package com.warehouse.driverhub.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.driverhub.application.port.secondary.CurrentOperatorServicePort;

public class CurrentOperatorServiceAdapter implements CurrentOperatorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public CurrentOperatorServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public OperatorId currentOperatorId() {
        return this.operatorContextProvider.currentOperatorId();
    }
}
