package com.warehouse.billing.payment.domain.service;

import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class PaymentFactory {

    private final Clock clock;

    public PaymentFactory(final Clock clock) {
        this.clock = clock;
    }

    public Payment create(final PriceQuoteId priceQuoteId, final Money amount, final Instant quotedAt,
                          final Duration paymentDeadline) {
        return new Payment(PaymentId.nextId(), priceQuoteId, amount, null, null, PaymentStatus.PENDING, null, 0,
                quotedAt.plus(paymentDeadline), this.clock.instant(), null, null, null, null, null, null, null, null,
                null);
    }
}
