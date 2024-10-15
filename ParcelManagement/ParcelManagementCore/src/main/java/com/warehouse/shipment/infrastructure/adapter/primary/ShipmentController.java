package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentStatusRequestDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.Status;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentResponseInformation;
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

	public ShipmentController(final ShipmentPort shipmentPort,
                              final ShipmentRequestValidator shipmentRequestValidator,
			                  final ShipmentRequestMapper requestMapper,
                              final ShipmentResponseMapper responseMapper) {
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

    @GetMapping("/{value}")
    public ResponseEntity<?> get(final ShipmentIdDto shipmentId) {
        shipmentRequestValidator.validateBody(shipmentId);
        final ShipmentId id = requestMapper.map(shipmentId);
        final Shipment shipment = shipmentPort.loadShipment(id);
        final ShipmentDto shipmentResponse = responseMapper.map(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentResponse);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody final ShipmentUpdateRequestDto shipmentUpdateRequest) {
        shipmentRequestValidator.validateBody(shipmentUpdateRequest);
        final ShipmentUpdateRequest request = requestMapper.map(shipmentUpdateRequest);
        shipmentPort.update(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestBody final ShipmentStatusRequestDto shipmentStatusRequest) {
        shipmentRequestValidator.validateBody(shipmentStatusRequest);
        final ShipmentStatusRequest request = requestMapper.map(shipmentStatusRequest);
        shipmentPort.updateShipmentStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @GetMapping("/exists/{value}")
    public ResponseEntity<?> existsShipment(final ShipmentIdDto shipmentId) {
        shipmentRequestValidator.validateBody(shipmentId);
        final ShipmentId id = requestMapper.map(shipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentPort.existsShipment(id));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(final EmptyRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
