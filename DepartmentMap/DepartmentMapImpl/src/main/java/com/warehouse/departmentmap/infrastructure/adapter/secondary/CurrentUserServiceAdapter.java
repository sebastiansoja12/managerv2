package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public CurrentUserServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public OperatorId currentOperatorId() {
        return this.operatorContextProvider.currentOperatorId();
    }

    @Override
    public UserId currentUserId() {
        return this.operatorContextProvider.currentUserId();
    }
}
