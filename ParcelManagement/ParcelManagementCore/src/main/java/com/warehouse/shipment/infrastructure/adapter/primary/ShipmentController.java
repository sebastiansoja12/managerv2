package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
@AllArgsConstructor
public class ShipmentController {

    private final ShipmentPort shipmentPort;

    @PostMapping
    public ShipmentResponse create(@RequestBody ShipmentRequest request) {
        return shipmentPort.ship(request);
    }

    @GetMapping("/{parcelId}")
    public Parcel get(@PathVariable Long parcelId) {
        return shipmentPort.loadParcel(parcelId);
    }
}
