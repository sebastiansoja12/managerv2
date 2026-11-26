package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.warehouse.billing.payment.infrastructure.adapter.primary.exception.InvalidWebhookSignatureException;

public class StripeWebhookEventVerifier {

    private final String webhookSecret;

    public StripeWebhookEventVerifier(final String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public Event verify(final String payload, final String signature) {
        if (signature == null || this.webhookSecret.isBlank()) {
            throw new InvalidWebhookSignatureException("Webhook signature is missing");
        }
        try {
            return Webhook.constructEvent(payload, signature, this.webhookSecret);
        } catch (final SignatureVerificationException exception) {
            throw new InvalidWebhookSignatureException("Webhook signature is invalid");
        }
    }
}
