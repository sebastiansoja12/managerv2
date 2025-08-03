package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.SignatureSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.service.ShipmentSignatureService;

@Component
public class ShipmentSignatureListener {

    private final ShipmentSignatureService shipmentSignatureService;

    public ShipmentSignatureListener(final ShipmentSignatureService shipmentSignatureService) {
        this.shipmentSignatureService = shipmentSignatureService;
    }

    @EventListener
    public void handle(final SignatureSigned event) {
        final SignatureSnapshot snapshot = event.getSnapshot();
        this.shipmentSignatureService.updateSignature(Signature.from(snapshot));
    }
}
