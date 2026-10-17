package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "shipment.ShipmentNoteEntity")
@Table(name = "shipment_note")
public class ShipmentNoteEntity extends BelongsToOperator {

    @Id
    @Column(name = "note_id", nullable = false)
    private UUID noteId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false))
    private ShipmentId shipmentId;

    @Column(name = "body", nullable = false, length = 4000)
    private String body;

    @Column(name = "internal", nullable = false)
    private boolean internal;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "author_user_id", nullable = false))
    private UserId authorUserId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ShipmentNoteEntity() {
    }

    public ShipmentNoteEntity(final UUID noteId, final ShipmentId shipmentId, final OperatorId operatorId,
                              final String body, final boolean internal, final UserId authorUserId,
                              final Instant createdAt) {
        this.noteId = noteId;
        this.shipmentId = shipmentId;
        assignOperator(operatorId);
        this.body = body;
        this.internal = internal;
        this.authorUserId = authorUserId;
        this.createdAt = createdAt;
    }

    public UUID getNoteId() { return noteId; }
    public ShipmentId getShipmentId() { return shipmentId; }
    public String getBody() { return body; }
    public boolean isInternal() { return internal; }
    public UserId getAuthorUserId() { return authorUserId; }
    public Instant getCreatedAt() { return createdAt; }
}
