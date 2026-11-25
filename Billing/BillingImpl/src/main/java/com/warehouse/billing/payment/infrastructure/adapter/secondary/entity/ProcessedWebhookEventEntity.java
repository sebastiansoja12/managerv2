package com.warehouse.billing.payment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.WebhookEventId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity(name = "billing.ProcessedWebhookEventEntity")
@Table(name = "processed_webhook_events")
public class ProcessedWebhookEventEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "event_id"))
    private WebhookEventId eventId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    public ProcessedWebhookEventEntity(final WebhookEventId eventId, final Instant processedAt) {
        this.eventId = eventId;
        this.processedAt = processedAt;
    }
}
