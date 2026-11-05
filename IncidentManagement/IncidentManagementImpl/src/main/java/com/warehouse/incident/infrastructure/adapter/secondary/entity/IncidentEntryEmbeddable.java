package com.warehouse.incident.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.incident.domain.enumeration.IncidentEntryType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Embeddable
public class IncidentEntryEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private IncidentEntryType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    @Column(name = "text", length = 2000)
    private String text;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    public IncidentEntryEmbeddable(final IncidentEntryType type,
                                   final UserId userId,
                                   final Username username,
                                   final String text,
                                   final Instant occurredAt) {
        this.type = type;
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.occurredAt = occurredAt;
    }
}
