package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.DangerousGoodCreateRequest;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.DangerousGoodValidator;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

import static com.warehouse.shipment.infrastructure.adapter.primary.validator.SignatureValidator.validateSignatureMethod;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentPort shipmentPort;
    
    private final ShipmentRequestValidator shipmentRequestValidator;

    private final DangerousGoodValidator dangerousGoodValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

	public ShipmentController(final ShipmentPort shipmentPort,
                              final ShipmentRequestValidator shipmentRequestValidator,
                              final DangerousGoodValidator dangerousGoodValidator,
                              final ShipmentRequestMapper requestMapper,
                              final ShipmentResponseMapper responseMapper) {
        this.shipmentPort = shipmentPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.dangerousGoodValidator = dangerousGoodValidator;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    @Counted(value = "controller.shipment.create")
    @Timed(value = "controller.shipment.create")
    public ResponseEntity<?> create(@RequestBody final ShipmentCreateRequestDto shipmentRequest) {
        shipmentRequestValidator.validateBody(shipmentRequest);
        final ShipmentCreateRequest request = requestMapper.map(shipmentRequest);
        final Result<ShipmentCreateResponse, ShipmentErrorCode> result = shipmentPort.ship(request);

        final ResponseEntity<?> response;
        if (result.isSuccess()) {
            response = ResponseEntity
                    .ok()
                    .body(responseMapper.map(result.getSuccess()));
        } else {
            response = ResponseEntity
                    .badRequest()
                    .body(result.getFailure());
        }
        return response;
    }

    @GetMapping("/{shipmentId}")
    @Counted(value = "controller.shipment.get")
    @Timed(value = "controller.shipment.get")
    public ResponseEntity<?> get(@PathVariable final ShipmentIdDto shipmentId) {
        final Shipment shipment = shipmentPort.loadShipment(new ShipmentId(shipmentId.getValue()));
        final ShipmentDto shipmentResponse = responseMapper.map(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentResponse);
    }

    @PutMapping
    @Counted(value = "controller.shipment.update")
    @Timed(value = "controller.shipment.update")
    public ResponseEntity<?> update(@RequestBody final ShipmentUpdateRequestDto shipmentUpdateRequest) {
        shipmentRequestValidator.validateBody(shipmentUpdateRequest);
        final ShipmentUpdateRequest request = requestMapper.map(shipmentUpdateRequest);
        final Result<Void, ShipmentErrorCode> result = shipmentPort.update(request);

        final ResponseEntity<?> response;
        if (result.isSuccess()) {
            response = ResponseEntity.status(HttpStatus.OK).build();
        } else {
            response = ResponseEntity.badRequest().body(result.getFailure());
        }
        return response;
    }
    
	@PutMapping("/dangerous-good/{shipmentId}")
    @Counted(value = "controller.dangerousgood.add")
    @Timed(value = "controller.dangerousgood.add")
	public ResponseEntity<?> addDangerousGood(
			@RequestBody final DangerousGoodCreateRequestDto dangerousGoodCreateRequest,
			@PathVariable final ShipmentId shipmentId) {
        dangerousGoodValidator.validateDangerousGood(dangerousGoodCreateRequest);

        final DangerousGoodCreateRequest request = DangerousGoodCreateRequest.from(dangerousGoodCreateRequest);
        final Result<Void, ShipmentErrorCode> result = shipmentPort.addDangerousGood(shipmentId, request);

        final ResponseEntity<?> response;
        if (result.isSuccess()) {
            response = ResponseEntity.status(HttpStatus.OK).build();
        } else {
            response = ResponseEntity.badRequest().body(result.getFailure());
        }
        return response;
    }

    @PutMapping("/status")
    @Counted(value = "controller.shipment.status.update")
    @Timed(value = "controller.shipment.status.update")
    public ResponseEntity<?> updateStatus(@RequestBody final ShipmentStatusRequestDto shipmentStatusRequest) {
        shipmentRequestValidator.validateBody(shipmentStatusRequest);
        final ShipmentStatusRequest request = requestMapper.map(shipmentStatusRequest);
        shipmentPort.changeShipmentStatusTo(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @PutMapping("/signature")
    @Counted(value = "controller.signature.add")
    @Timed(value = "controller.signature.add")
    public ResponseEntity<?> changeSignature(@RequestBody final SignatureChangeRequestDto signatureChangeRequest,
                                             @Param("signatureMethod") final String signatureMethod) {
        validateSignatureMethod(signatureMethod);
        shipmentRequestValidator.validateBody(signatureChangeRequest);
        final SignatureChangeRequest request = requestMapper.map(signatureChangeRequest);
        shipmentPort.changeShipmentSignatureTo(request, SignatureMethod.valueOf(signatureMethod));
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @GetMapping("/exists/{shipmentId}")
    @Counted(value = "controller.shipment.exist")
    @Timed(value = "controller.shipment.exist")
    public ResponseEntity<?> existsShipment(@PathVariable final ShipmentIdDto shipmentId) {
        shipmentRequestValidator.validateBody(shipmentId);
        final ShipmentId id = requestMapper.map(shipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentPort.existsShipment(id));
    }

    @PutMapping("/person")
    @Counted(value = "controller.person.update")
    @Timed(value = "controller.person.update")
    public ResponseEntity<?> updatePerson(@RequestBody final PersonDto personRequest,
                                          @RequestParam("shipmentId") @PathVariable final ShipmentIdDto shipmentId,
                                          @RequestParam("personType") final PersonType personType) {
        final Person person = personType == PersonType.SENDER ? Sender.from(personRequest) : Recipient.from(personRequest);
        this.shipmentPort.changePersonTo(person, new ShipmentId(shipmentId.getValue()));
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(final EmptyRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
