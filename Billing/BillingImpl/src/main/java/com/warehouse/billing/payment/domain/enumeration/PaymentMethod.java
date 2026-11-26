package com.warehouse.billing.payment.domain.enumeration;

public enum PaymentMethod {
    CASH(PaymentChannel.COUNTER),
    CARD(PaymentChannel.COUNTER),
    STRIPE(PaymentChannel.ONLINE);

    private final PaymentChannel channel;

    PaymentMethod(final PaymentChannel channel) {
        this.channel = channel;
    }

    public PaymentChannel channel() {
        return channel;
    }
}
