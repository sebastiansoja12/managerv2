package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.secondary.ProcessedWebhookRepository;
import com.warehouse.commonassets.identificator.WebhookEventId;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

class InMemoryProcessedWebhookRepository implements ProcessedWebhookRepository {

    private final Map<WebhookEventId, Instant> processed = new HashMap<>();

    @Override
    public boolean isProcessed(final WebhookEventId webhookEventId) {
        return this.processed.containsKey(webhookEventId);
    }

    @Override
    public void markProcessed(final WebhookEventId webhookEventId, final Instant processedAt) {
        this.processed.put(webhookEventId, processedAt);
    }
}
