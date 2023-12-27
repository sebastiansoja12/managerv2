package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.infrastructure.api.dto.ParcelDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentPort shipmentPort;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ShipmentRequestDto shipmentRequest) {
        final ShipmentRequest request = requestMapper.map(shipmentRequest);
        final ShipmentResponse shipmentResponse = shipmentPort.ship(request);
        return new ResponseEntity<>(responseMapper.map(shipmentResponse), HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> get(@PathVariable Long parcelId) {
        final Parcel parcel = shipmentPort.loadParcel(parcelId);
        final ParcelDto parcelResponse = responseMapper.map(parcel);
        return new ResponseEntity<>(parcelResponse, HttpStatus.OK);
    }
}
