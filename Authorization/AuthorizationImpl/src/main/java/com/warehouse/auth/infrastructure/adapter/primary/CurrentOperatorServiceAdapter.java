package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.auth.domain.port.primary.CurrentOperatorPort;
import com.warehouse.commonassets.identificator.OperatorId;

public class CurrentOperatorServiceAdapter implements CurrentOperatorService {

    private final CurrentOperatorPort currentOperatorPort;

    public CurrentOperatorServiceAdapter(final CurrentOperatorPort currentOperatorPort) {
        this.currentOperatorPort = currentOperatorPort;
    }

    @Override
    public OperatorId getCurrentOperatorId() {
        return currentOperatorPort.getCurrentOperatorId();
    }
}
