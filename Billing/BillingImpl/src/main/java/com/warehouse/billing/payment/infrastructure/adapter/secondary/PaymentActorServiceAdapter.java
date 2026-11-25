package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.domain.vo.PaymentActor;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;

public class PaymentActorServiceAdapter implements PaymentActorServicePort {

    private final OperatorContextProvider operatorContextProvider;

    public PaymentActorServiceAdapter(final OperatorContextProvider operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public PaymentActor currentActor() {
        final OperatorDetails details = this.operatorContextProvider.currentContext()
                .orElseThrow(() -> new IllegalStateException("User context is required"));
        return new PaymentActor(details.userId(), details.departmentId());
    }
}
