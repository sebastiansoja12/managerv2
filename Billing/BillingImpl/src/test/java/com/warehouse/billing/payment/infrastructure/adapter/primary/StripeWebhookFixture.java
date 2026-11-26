package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.stripe.Stripe;
import com.stripe.net.Webhook;

import java.security.GeneralSecurityException;

final class StripeWebhookFixture {

    static final String WEBHOOK_SECRET = "whsec_test_secret";

    private StripeWebhookFixture() {
    }

    static String checkoutEvent(final String eventId, final String type, final String sessionId,
                                final String paymentStatus) {
        return """
                {"id":"%s","object":"event","api_version":"%s","created":1795000000,"type":"%s",\
                "data":{"object":{"id":"%s","object":"checkout.session","payment_status":"%s",\
                "payment_intent":"pi_test_1","metadata":{"paymentId":"1"}}}}"""
                .formatted(eventId, Stripe.API_VERSION, type, sessionId, paymentStatus);
    }

    static String chargeRefundedEvent(final String eventId, final String paymentIntent) {
        return """
                {"id":"%s","object":"event","api_version":"%s","created":1795000000,"type":"charge.refunded",\
                "data":{"object":{"id":"ch_1","object":"charge","refunded":true,"payment_intent":"%s"}}}"""
                .formatted(eventId, Stripe.API_VERSION, paymentIntent);
    }

    static String signature(final String payload, final String secret) throws GeneralSecurityException {
        final long timestamp = Webhook.Util.getTimeNow();
        return "t=" + timestamp + ",v1=" + Webhook.Util.computeHmacSha256(secret, timestamp + "." + payload);
    }
}
