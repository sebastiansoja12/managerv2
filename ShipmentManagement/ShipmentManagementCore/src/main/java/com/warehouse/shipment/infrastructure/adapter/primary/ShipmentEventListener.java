package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.model.DangerousGoodCreateCommand;
import com.warehouse.shipment.domain.vo.DangerousGoodId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.service.ShipmentSignatureService;
import com.warehouse.shipment.domain.vo.SignatureSnapshot;
import com.warehouse.shipment.infrastructure.adapter.primary.event.DangerousGoodCreated;

import java.util.Collections;

@Component("shipment.shipmentEventListener")
public class ShipmentEventListener {

    private final ShipmentSignatureService shipmentSignatureService;

    private final ShipmentPort shipmentPort;

    public ShipmentEventListener(final ShipmentSignatureService shipmentSignatureService,
                                 final ShipmentPort shipmentPort) {
        this.shipmentSignatureService = shipmentSignatureService;
        this.shipmentPort = shipmentPort;
    }

    @EventListener
    public void handle(final SignatureSigned event) {
        final SignatureSnapshot snapshot = event.getSnapshot();
        this.shipmentSignatureService.updateSignature(Signature.from(snapshot));
    }

    @EventListener
    public void handle(final DangerousGoodCreated event) {
        final GoodSnapshot goodSnapshot = event.getSnapshot();
		final DangerousGoodCreateCommand request = new DangerousGoodCreateCommand(
				new DangerousGoodId(goodSnapshot.dangerousGoodId().value()), goodSnapshot.shipmentId(),
				goodSnapshot.name(), goodSnapshot.description(), goodSnapshot.classificationCode().name(),
				Collections.singletonList(goodSnapshot.hazardSymbols()), goodSnapshot.storageRequirement().name(),
				goodSnapshot.handlingInstructions(), goodSnapshot.weight(), goodSnapshot.packaging().name(),
				goodSnapshot.flammable(), goodSnapshot.corrosive(), goodSnapshot.toxic(),
				goodSnapshot.emergencyContact(), goodSnapshot.countryOfOrigin(), goodSnapshot.safetyDataSheet());
        this.shipmentPort.addDangerousGood(request);
    }
}
