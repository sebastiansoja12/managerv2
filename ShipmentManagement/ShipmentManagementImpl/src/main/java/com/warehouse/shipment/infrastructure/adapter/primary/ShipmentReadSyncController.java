package com.warehouse.shipment.infrastructure.adapter.primary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.domain.service.ShipmentReadModelSyncService;

@RestController
@RequestMapping("/internal/shipments/read-sync")
class ShipmentReadSyncController {

    private final ShipmentReadModelSyncService syncService;

    private final OperatorContext operatorContext;

    ShipmentReadSyncController(final ShipmentReadModelSyncService syncService,
                               final OperatorContext operatorContext) {
        this.syncService = syncService;
        this.operatorContext = operatorContext;
    }

    @PostMapping("/{operatorId}")
    ResponseEntity<ShipmentReadSyncResponse> sync(@PathVariable final Long operatorId) {
        return operatorContext.runAs(OperatorId.of(operatorId), () -> {
            final int syncedShipments = syncService.syncReadModels();
            return ResponseEntity.ok(new ShipmentReadSyncResponse(operatorId, syncedShipments));
        });
    }

    record ShipmentReadSyncResponse(Long operatorId, int syncedShipments) {
    }
}
