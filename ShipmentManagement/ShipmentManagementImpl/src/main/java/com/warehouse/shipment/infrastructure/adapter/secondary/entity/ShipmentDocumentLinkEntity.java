package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity(name = "shipment.ShipmentDocumentLinkEntity")
@Table(name = "shipment_document")
public class ShipmentDocumentLinkEntity extends BelongsToOperator {

    @Id
    @Column(name = "shipment_document_id", nullable = false)
    private UUID shipmentDocumentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false))
    private ShipmentId shipmentId;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    protected ShipmentDocumentLinkEntity() {
    }

    public ShipmentDocumentLinkEntity(final UUID shipmentDocumentId, final ShipmentId shipmentId,
                                      final UUID documentId, final OperatorId operatorId) {
        this.shipmentDocumentId = shipmentDocumentId;
        this.shipmentId = shipmentId;
        this.documentId = documentId;
        assignOperator(operatorId);
    }
}
