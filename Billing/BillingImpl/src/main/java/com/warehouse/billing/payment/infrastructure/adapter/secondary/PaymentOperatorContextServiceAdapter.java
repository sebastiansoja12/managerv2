package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.function.Supplier;

public class PaymentOperatorContextServiceAdapter implements PaymentOperatorContextServicePort {

    private final OperatorContext operatorContext;

    public PaymentOperatorContextServiceAdapter(final OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    @Override
    public <T> T runAs(final OperatorId operatorId, final Supplier<T> operation) {
        return this.operatorContext.runAs(operatorId, operation);
    }

    @Override
    public void runAs(final OperatorId operatorId, final Runnable operation) {
        this.operatorContext.runAs(operatorId, operation);
    }
}
