package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record WebhookEventId(String value) implements Serializable {
}
