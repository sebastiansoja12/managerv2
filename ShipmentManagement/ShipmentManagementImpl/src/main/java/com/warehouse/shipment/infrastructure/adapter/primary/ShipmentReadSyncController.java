package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;

@RestController
@RequestMapping("/internal/shipments/read-sync")
class ShipmentReadSyncController {

    private final ShipmentReadModelSyncPort syncPort;

    private final OperatorContext operatorContext;

    ShipmentReadSyncController(final ShipmentReadModelSyncPort syncPort,
                               final OperatorContext operatorContext) {
        this.syncPort = syncPort;
        this.operatorContext = operatorContext;
    }

    @PostMapping("/{operatorId}/{shipmentId}")
    ResponseEntity<?> syncReadModelForShipment(@PathVariable final Long operatorId, @PathVariable final Long shipmentId) {
        operatorContext.runAs(OperatorId.of(operatorId), () -> {
            syncPort.syncReadModel(new ShipmentId(shipmentId));
        });
        return ResponseEntity.ok().build();
    }
}
