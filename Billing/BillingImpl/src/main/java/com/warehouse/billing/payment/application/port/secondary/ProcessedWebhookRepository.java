package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.commonassets.identificator.WebhookEventId;

import java.time.Instant;

public interface ProcessedWebhookRepository {

    boolean isProcessed(final WebhookEventId webhookEventId);

    void markProcessed(final WebhookEventId webhookEventId, final Instant processedAt);
}
