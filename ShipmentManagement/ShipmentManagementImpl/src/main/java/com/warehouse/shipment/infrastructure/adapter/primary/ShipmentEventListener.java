package com.warehouse.shipment.infrastructure.adapter.primary;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.service.ShipmentSignatureService;
import com.warehouse.shipment.domain.vo.SignatureSnapshot;

@Component("shipment.shipmentEventListener")
public class ShipmentEventListener {

    private final ShipmentSignatureService shipmentSignatureService;

    public ShipmentEventListener(final ShipmentSignatureService shipmentSignatureService) {
        this.shipmentSignatureService = shipmentSignatureService;
    }

    @EventListener
    public void handle(final SignatureSigned event) {
        final SignatureSnapshot snapshot = event.getSnapshot();
        this.shipmentSignatureService.updateSignature(Signature.from(snapshot));
    }
}
