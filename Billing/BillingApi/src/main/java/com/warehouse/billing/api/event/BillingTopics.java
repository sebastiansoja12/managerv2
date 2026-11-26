package com.warehouse.billing.api.event;

public final class BillingTopics {

    public static final String PRICE_QUOTE_PAID = "billing.price-quote-paid";
    public static final String PAYMENT_EXPIRED = "billing.payment-expired";
    public static final String PAYMENT_REFUNDED = "billing.payment-refunded";
    public static final String INVOICE_ISSUED = "billing.invoice-issued";

    private BillingTopics() {
    }
}
