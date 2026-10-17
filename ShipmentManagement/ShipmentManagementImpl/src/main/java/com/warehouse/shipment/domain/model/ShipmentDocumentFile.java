package com.warehouse.shipment.domain.model;

public record ShipmentDocumentFile(ShipmentDocument document, byte[] content) {

    public ShipmentDocumentFile {
        content = content.clone();
    }

    @Override
    public byte[] content() {
        return content.clone();
    }
}
