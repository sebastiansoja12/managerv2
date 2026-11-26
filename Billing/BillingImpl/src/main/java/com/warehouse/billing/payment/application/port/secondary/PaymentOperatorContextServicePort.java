package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.function.Supplier;

public interface PaymentOperatorContextServicePort {

    <T> T runAs(final OperatorId operatorId, final Supplier<T> operation);

    void runAs(final OperatorId operatorId, final Runnable operation);
}
