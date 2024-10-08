package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.map(shipmentResponse));
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> get(@PathVariable final ShipmentIdDto parcelId) {
        shipmentRequestValidator.validateBody(parcelId);
        final ShipmentId id = requestMapper.map(parcelId);
        final Parcel parcel = shipmentPort.loadParcel(id);
        final ShipmentDto parcelResponse = responseMapper.map(parcel);
        return ResponseEntity.status(HttpStatus.OK).body(parcelResponse);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody final ShipmentUpdateRequestDto shipmentUpdateRequest) {
        shipmentRequestValidator.validateBody(shipmentUpdateRequest);
        final ShipmentUpdateRequest request = requestMapper.map(shipmentUpdateRequest);
        final ShipmentUpdateResponse response = shipmentPort.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(responseMapper.map(response));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(final EmptyRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
