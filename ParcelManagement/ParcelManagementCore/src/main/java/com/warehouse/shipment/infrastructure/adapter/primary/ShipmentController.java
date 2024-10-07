package com.warehouse.shipment.infrastructure.adapter.primary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.api.dto.ParcelDto;
import com.warehouse.shipment.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentPort shipmentPort;
    
    private final ShipmentRequestValidator shipmentRequestValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

	public ShipmentController(final ShipmentPort shipmentPort, final ShipmentRequestValidator shipmentRequestValidator,
			final ShipmentRequestMapper requestMapper, final ShipmentResponseMapper responseMapper) {
        this.shipmentPort = shipmentPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final ShipmentRequestDto shipmentRequest) {
        shipmentRequestValidator.validateBody(shipmentRequest);
        final ShipmentRequest request = requestMapper.map(shipmentRequest);
        final ShipmentResponse shipmentResponse = shipmentPort.ship(request);
        return new ResponseEntity<>(responseMapper.map(shipmentResponse), HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> get(@PathVariable final ParcelIdDto parcelId) {
        shipmentRequestValidator.validateBody(parcelId);
        final ParcelId id = requestMapper.map(parcelId);
        final Parcel parcel = shipmentPort.loadParcel(id);
        final ParcelDto parcelResponse = responseMapper.map(parcel);
        return new ResponseEntity<>(parcelResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody final ShipmentUpdateRequestDto shipmentUpdateRequest) {
        shipmentRequestValidator.validateBody(shipmentUpdateRequest);
        final ShipmentUpdateRequest request = requestMapper.map(shipmentUpdateRequest);
        final ShipmentUpdateResponse response = shipmentPort.update(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(final EmptyRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
