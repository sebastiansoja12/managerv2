package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.organisationstructure.subscription.application.port.secondary.CurrentOperatorServicePort;

import java.util.Optional;

public class CurrentOperatorServiceAdapter implements CurrentOperatorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public CurrentOperatorServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public Optional<OperatorId> currentOperatorId() {
        return operatorContextProvider.currentContext()
                .map(OperatorDetails::operatorId);
    }
}
