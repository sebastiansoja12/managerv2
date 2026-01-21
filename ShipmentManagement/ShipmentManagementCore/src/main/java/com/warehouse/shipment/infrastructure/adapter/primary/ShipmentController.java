package com.warehouse.shipment.infrastructure.adapter.primary;

import static com.warehouse.shipment.infrastructure.adapter.primary.validator.SignatureValidator.validateSignatureMethod;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.DangerousGoodValidator;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

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
    public ResponseEntity<?> create(@RequestBody final ShipmentCreateRequestApi shipmentRequest) {
        shipmentRequestValidator.validateBody(shipmentRequest);
        final ShipmentCreateCommand request = requestMapper.map(shipmentRequest);
        final Result<ShipmentCreateResponse, ErrorCode> result = shipmentPort.ship(request);

        final ResponseEntity<?> response;
        if (result.isSuccess()) {
            response = ResponseEntity
                    .ok()
                    .body(responseMapper.map(result.getSuccess()));
        } else {
            response = ResponseEntity
                    .badRequest()
                    .body(result.getFailure().getMessage());
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
    public ResponseEntity<?> update(@RequestBody final ShipmentUpdateRequestApi shipmentUpdateRequest) {
        shipmentRequestValidator.validateBody(shipmentUpdateRequest);
        final ShipmentUpdateCommand request = requestMapper.map(shipmentUpdateRequest);
        final Result<Void, ErrorCode> result = shipmentPort.update(request);

        final ResponseEntity<?> response;
        if (result.isSuccess()) {
            response = ResponseEntity.status(HttpStatus.OK).build();
        } else {
            response = ResponseEntity.badRequest().body(result.getFailure());
        }
        return response;
    }

    @PutMapping("/returns")
    @Counted(value = "controller.shipment.return")
    @Timed(value = "controller.shipment.return")
    public ResponseEntity<?> returnShipment(@RequestBody final ShipmentReturnRequestApi shipmentReturnRequest) {
        final ShipmentReturnCommand request = ShipmentReturnCommand.from(shipmentReturnRequest);
        this.shipmentPort.processShipmentReturn(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @PutMapping("/deliveries")
    @Counted(value = "controller.shipment.delivery")
    @Timed(value = "controller.shipment.delivery")
    public ResponseEntity<?> deliverShipment(@RequestBody final ShipmentDeliveryRequestApiDto deliveryRequest) {
        final ShipmentDeliveryCommand command = requestMapper.map(deliveryRequest);
        this.shipmentPort.processShipmentDelivery(command);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }
    
	// disabled
    @Counted(value = "controller.dangerousgood.add")
    @Timed(value = "controller.dangerousgood.add")
	public ResponseEntity<?> addDangerousGood(
			@RequestBody final DangerousGoodCreateRequestApi dangerousGoodCreateRequest) {
        dangerousGoodValidator.validateDangerousGood(dangerousGoodCreateRequest);

        final DangerousGoodCreateCommand request = DangerousGoodCreateCommand.from(dangerousGoodCreateRequest);
        final Result<Void, ErrorCode> result = shipmentPort.addDangerousGood(request);

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
    public ResponseEntity<?> updateStatus(@RequestBody final ShipmentStatusRequestApi shipmentStatusRequest) {
        shipmentRequestValidator.validateBody(shipmentStatusRequest);
        final ShipmentStatusRequest request = requestMapper.map(shipmentStatusRequest);
        shipmentPort.changeShipmentStatusTo(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @PutMapping("/signature")
    @Counted(value = "controller.signature.add")
    @Timed(value = "controller.signature.add")
    public ResponseEntity<?> changeSignature(@RequestBody final SignatureChangeRequestApi signatureChangeRequest,
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
    public ResponseEntity<?> updatePerson(@RequestBody final PersonApi personRequest,
                                          @RequestParam("shipmentId") @PathVariable final ShipmentIdDto shipmentId,
                                          @RequestParam("personType") final PersonType personType) {
        final Person person = personType == PersonType.SENDER ? Sender.from(personRequest) : Recipient.from(personRequest);
        this.shipmentPort.changePersonTo(person, new ShipmentId(shipmentId.getValue()));
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @PutMapping("/countries")
    @Counted(value = "controller.country.update")
    @Timed(value = "controller.country.update")
    public ResponseEntity<?> updatePerson(@RequestBody final CountryRequestApi countryRequestApi,
                                          @RequestParam("shipmentId") final Long shipmentId) {
        final ShipmentId id = new ShipmentId(shipmentId);
		final ShipmentCountryRequest request = new ShipmentCountryRequest(id,
				CountryCode.valueOf(countryRequestApi.issuerCountryCode()), CountryCode.valueOf(countryRequestApi.receiverCountryCode()));
        this.shipmentPort.changeShipmentCountries(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }
    
    @PutMapping("/shipment-type")
    @Counted(value = "controller.shipment.type.update")
    @Timed(value = "controller.shipment.type.update")
	public ResponseEntity<?> changeShipmentType(@RequestParam("shipmentType") final ShipmentType shipmentType,
                                                @RequestParam("shipmentId") @PathVariable final String shipmentId) {
        final ChangeShipmentTypeRequest request = new ChangeShipmentTypeRequest(new ShipmentId(Long.parseLong(shipmentId)), shipmentType);
        this.shipmentPort.changeShipmentTypeTo(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(final EmptyRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ShipmentValidationException.class)
    public ResponseEntity<?> handleException(final ShipmentValidationException exception) {
        return ResponseEntity.status(exception.getCode()).body(exception.getValidationErrors());
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<?> handleException(final TechnicalException exception) {
        return ResponseEntity.status(exception.getCode()).body(exception.getMessage());
    }
}
