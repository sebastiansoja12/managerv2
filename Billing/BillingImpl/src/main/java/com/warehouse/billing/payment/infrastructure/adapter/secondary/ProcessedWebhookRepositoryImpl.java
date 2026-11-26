package com.warehouse.billing.payment.infrastructure.adapter.secondary;

import com.warehouse.billing.payment.application.port.secondary.ProcessedWebhookRepository;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.ProcessedWebhookEventEntity;
import com.warehouse.commonassets.identificator.WebhookEventId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.time.Instant;

public class ProcessedWebhookRepositoryImpl implements ProcessedWebhookRepository {

    private final OperatorFilteredRepository<ProcessedWebhookEventEntity> repository;

    public ProcessedWebhookRepositoryImpl(final OperatorFilteredRepository<ProcessedWebhookEventEntity> repository) {
        this.repository = repository;
    }

    @Override
    public boolean isProcessed(final WebhookEventId webhookEventId) {
        return this.repository.createCriteria(ProcessedWebhookEventEntity.class)
                .eq("eventId.value", webhookEventId.value())
                .one()
                .isPresent();
    }

    @Override
    public void markProcessed(final WebhookEventId webhookEventId, final Instant processedAt) {
        this.repository.create(new ProcessedWebhookEventEntity(webhookEventId, processedAt));
    }
}
