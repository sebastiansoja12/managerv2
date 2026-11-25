package com.warehouse.billing.payment.domain.service;

import com.warehouse.billing.payment.domain.model.LateChargeRefund;
import com.warehouse.billing.payment.domain.model.Payment;

import java.time.Clock;

public class LateChargeRefundFactory {

    private final Clock clock;

    public LateChargeRefundFactory(final Clock clock) {
        this.clock = clock;
    }

    public LateChargeRefund create(final Payment payment, final String gatewayReference,
                                   final String gatewayPaymentIntent) {
        return new LateChargeRefund(gatewayReference, gatewayPaymentIntent, payment.id(), payment.amount(),
                this.clock.instant());
    }
}
