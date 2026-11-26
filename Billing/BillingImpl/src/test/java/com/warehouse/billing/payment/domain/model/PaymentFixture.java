package com.warehouse.billing.payment.domain.model;

import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

public final class PaymentFixture {

    public static final PriceQuoteId PRICE_QUOTE_ID = new PriceQuoteId(20L);

    public static final UserId USER_ID = new UserId(5L);

    public static final DepartmentId DEPARTMENT_ID = new DepartmentId(100L);

    public static final Instant QUOTED_AT = Instant.parse("2026-11-24T08:00:00Z");

    public static final Instant NOW = Instant.parse("2026-11-24T12:00:00Z");

    private PaymentFixture() {
    }

    public static Payment pending() {
        return withStatus(PaymentStatus.PENDING);
    }

    public static Payment newPending() {
        return payment(PaymentStatus.PENDING, null);
    }

    public static Payment newPending(final PaymentId paymentId, final PriceQuoteId priceQuoteId) {
        return new Payment(paymentId, priceQuoteId, Money.from(new BigDecimal("17.22"), Currency.PLN), null, null,
                PaymentStatus.PENDING, null, 0, QUOTED_AT.plusSeconds(72 * 3600), NOW, null, null, null, null, null,
                null, null, null, null);
    }

    public static Payment pendingDue(final PaymentId paymentId, final PriceQuoteId priceQuoteId,
                                     final Instant deadline) {
        return new Payment(paymentId, priceQuoteId, Money.from(new BigDecimal("17.22"), Currency.PLN), null, null,
                PaymentStatus.PENDING, null, 0, deadline, QUOTED_AT, null, null, null, null, null, null, null, null,
                null);
    }

    public static Payment withStatus(final PaymentStatus status) {
        return payment(status, 0L);
    }

    private static Payment payment(final PaymentStatus status, final Long version) {
        return new Payment(new PaymentId(1L), PRICE_QUOTE_ID, Money.from(new BigDecimal("17.22"), Currency.PLN),
                null, null, status, null, 0, QUOTED_AT.plusSeconds(72 * 3600), NOW, null, null, null, null, null, null,
                null, null, version);
    }
}
