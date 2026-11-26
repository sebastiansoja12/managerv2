package com.warehouse.billing.payment.domain.vo;

import java.time.Instant;

public record CheckoutSession(String gatewayReference, String url, Instant expiresAt) {

    public boolean isOpenAt(final Instant now) {
        return this.expiresAt.isAfter(now);
    }
}
