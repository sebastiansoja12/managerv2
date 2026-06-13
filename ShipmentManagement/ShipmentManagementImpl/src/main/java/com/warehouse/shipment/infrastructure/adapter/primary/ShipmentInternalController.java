package com.warehouse.shipment.infrastructure.adapter.primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.DangerousGoodNotFoundException;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.ShipmentConfigurationServicePort;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.warehouse.shipment.infrastructure.adapter.primary.validator.SignatureValidator.validateSignatureMethod;

@RestController
@RequestMapping("/shipments")
public class ShipmentInternalController {

    private final ShipmentPort shipmentPort;
    
    private final ShipmentRequestValidator shipmentRequestValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

    private final ObjectMapper objectMapper;

    private final ShipmentConfigurationServicePort shipmentConfigurationServicePort;

	public ShipmentInternalController(final ShipmentPort shipmentPort,
                                      final ShipmentRequestValidator shipmentRequestValidator,
                                      final ShipmentRequestMapper requestMapper,
                                      final ShipmentResponseMapper responseMapper,
                                      final ObjectMapper objectMapper,
                                      final ShipmentConfigurationServicePort shipmentConfigurationServicePort) {
        this.shipmentPort = shipmentPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.objectMapper = objectMapper;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
    }

    @PostMapping
    @Counted(value = "controller.shipment.create")
    @Timed(value = "controller.shipment.create")
    public ResponseEntity<?> create(@RequestBody final ShipmentCreateRequestApi shipmentRequest) {
        final ShipmentValidationRules validationRules =
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration().validationRules();
        shipmentRequestValidator.validateRequest(shipmentRequest, validationRules);
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

    @PutMapping("/cancel/{id}")
    @Counted(value = "controller.shipment.cancel")
    @Timed(value = "controller.shipment.cancel")
    public ResponseEntity<?> cancel(@PathVariable final Long id) {
        final ShipmentId shipmentId = new ShipmentId(id);
        this.shipmentPort.cancel(shipmentId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    @Counted(value = "controller.shipment.list")
    @Timed(value = "controller.shipment.list")
    public ResponseEntity<?> search(@RequestBody(required = false) final ShipmentSearchRequestApi request) {
        final ShipmentSearchCriteria criteria = requestMapper.map(request);
        final List<ShipmentDto> shipmentResponse = shipmentPort.searchShipments(criteria).stream()
                .map(responseMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(shipmentResponse);
    }

    @GetMapping("/{shipmentId}")
    @Counted(value = "controller.shipment.get")
    @Timed(value = "controller.shipment.get")
    public ResponseEntity<?> get(@PathVariable final Long shipmentId) {
        final Shipment shipment = shipmentPort.loadShipment(new ShipmentId(shipmentId));
        final ShipmentDto shipmentResponse = responseMapper.map(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentResponse);
    }

    @GetMapping("/{shipmentId}/control-center")
    @Counted(value = "controller.shipment.controlcenter.get")
    @Timed(value = "controller.shipment.controlcenter.get")
    public ResponseEntity<?> getControlCenter(@PathVariable final Long shipmentId) {
        final ShipmentRouteLog controlCenter = shipmentPort.getShipmentByShipmentId(new ShipmentId(shipmentId));
        return ResponseEntity.status(HttpStatus.OK).body(responseMapper.map(controlCenter));
    }

    @GetMapping("/tracking-numbers/{trackingNumber}")
    @Counted(value = "controller.shipment.trackingnumber.get")
    @Timed(value = "controller.shipment.trackingnumber.get")
    public ResponseEntity<?> getByTrackingNumber(@PathVariable final String trackingNumber) {
        final Shipment shipment = shipmentPort.loadShipment(new TrackingNumber(trackingNumber));
        final ShipmentDto shipmentResponse = responseMapper.map(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(shipmentResponse);
    }

    @GetMapping("/tracking-numbers/{trackingNumber}/control-center")
    @Counted(value = "controller.shipment.trackingnumber.controlcenter.get")
    @Timed(value = "controller.shipment.trackingnumber.controlcenter.get")
    public ResponseEntity<?> getControlCenterByTrackingNumber(@PathVariable final String trackingNumber) {
        final ShipmentRouteLog shipmentRouteLog = shipmentPort.getShipmenyByTrackingNumber(new TrackingNumber(trackingNumber));
        return ResponseEntity.status(HttpStatus.OK).body(responseMapper.map(shipmentRouteLog));
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

    @GetMapping("/returns/{returnPackageId}")
    @Counted(value = "controller.shipment.return.get")
    @Timed(value = "controller.shipment.return.get")
    public ResponseEntity<ShipmentReturnDetailsApi> getReturn(@PathVariable final Long returnPackageId) {
        final ShipmentReturnDetails response = this.shipmentPort.loadShipmentReturn(new ReturnId(returnPackageId));
        return ResponseEntity.ok(this.responseMapper.map(response));
    }

    @DeleteMapping("/returns/{returnPackageId}")
    @Counted(value = "controller.shipment.return.cancel")
    @Timed(value = "controller.shipment.return.cancel")
    public ResponseEntity<ShipmentResponseInformation> cancelReturn(@PathVariable final Long returnPackageId) {
        this.shipmentPort.cancelShipmentReturn(new ReturnId(returnPackageId));
        return ResponseEntity.ok(new ShipmentResponseInformation(Status.OK));
    }

    @GetMapping("/returns")
    @Counted(value = "controller.shipment.returns.get")
    @Timed(value = "controller.shipment.returns.get")
    public ResponseEntity<ShipmentReturnPageApi> getReturns(
            @RequestParam final String departmentCode,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "50") final int size) {
        if (departmentCode.isBlank()) {
            throw new IllegalArgumentException("Department code is required");
        }
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Page must be non-negative and size must be between 1 and 100");
        }
        final ShipmentReturnPage response = this.shipmentPort.loadShipmentReturns(
                new DepartmentCode(departmentCode.trim().toUpperCase()), page, size);
        return ResponseEntity.ok(this.responseMapper.map(response));
    }

    @PutMapping("/deliveries")
    @Counted(value = "controller.shipment.delivery")
    @Timed(value = "controller.shipment.delivery")
    public ResponseEntity<?> deliverShipment(@RequestBody final ShipmentDeliveryRequestApiDto deliveryRequest) {
        final ShipmentDeliveryCommand command = requestMapper.map(deliveryRequest);
        this.shipmentPort.processShipmentDelivery(command);
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }
    
    @GetMapping("/{shipmentId}/dangerous-good")
    @Counted(value = "controller.shipment.dangerousgood.get")
    @Timed(value = "controller.shipment.dangerousgood.get")
    public ResponseEntity<DangerousGoodApi> getDangerousGood(@PathVariable final Long shipmentId) {
        return shipmentPort.loadDangerousGood(new ShipmentId(shipmentId))
                .map(responseMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{shipmentId}/dangerous-good")
    @Counted(value = "controller.shipment.dangerousgood.put")
    @Timed(value = "controller.shipment.dangerousgood.put")
    public ResponseEntity<DangerousGoodApi> putDangerousGood(
            @PathVariable final Long shipmentId,
            @RequestBody final DangerousGoodApi dangerousGoodRequest) {
        if (dangerousGoodRequest == null) {
            throw new IllegalArgumentException("Dangerous goods request body is required");
        }
        final DangerousGood dangerousGood = requestMapper.map(dangerousGoodRequest);
        shipmentPort.putDangerousGood(new ShipmentId(shipmentId), dangerousGood);
        return ResponseEntity.ok(responseMapper.map(dangerousGood));
    }

    @PatchMapping("/{shipmentId}/dangerous-good")
    @Counted(value = "controller.shipment.dangerousgood.patch")
    @Timed(value = "controller.shipment.dangerousgood.patch")
    public ResponseEntity<DangerousGoodApi> patchDangerousGood(
            @PathVariable final Long shipmentId,
            @RequestBody final JsonNode dangerousGoodPatch) {
        if (dangerousGoodPatch == null || !dangerousGoodPatch.isObject()) {
            throw new IllegalArgumentException("Dangerous goods patch must be a JSON object");
        }
        final ShipmentId id = new ShipmentId(shipmentId);
        final DangerousGood current = shipmentPort.loadDangerousGood(id)
                .orElseThrow(() -> new DangerousGoodNotFoundException(
                        "Dangerous goods were not found for shipment " + shipmentId));
        if (dangerousGoodPatch.isEmpty()) {
            return ResponseEntity.ok(responseMapper.map(current));
        }
        final DangerousGoodApi mergedRequest = mergeDangerousGoodPatch(current, dangerousGoodPatch);
        final DangerousGood updated = requestMapper.map(mergedRequest);
        shipmentPort.putDangerousGood(id, updated);
        return ResponseEntity.ok(responseMapper.map(updated));
    }

    @DeleteMapping("/{shipmentId}/dangerous-good")
    @Counted(value = "controller.shipment.dangerousgood.delete")
    @Timed(value = "controller.shipment.dangerousgood.delete")
    public ResponseEntity<Void> deleteDangerousGood(@PathVariable final Long shipmentId) {
        shipmentPort.deleteDangerousGood(new ShipmentId(shipmentId));
        return ResponseEntity.noContent().build();
    }

    private DangerousGoodApi mergeDangerousGoodPatch(
            final DangerousGood current,
            final JsonNode dangerousGoodPatch
    ) {
        final ObjectNode merged = objectMapper.valueToTree(responseMapper.map(current));
        final Iterator<Map.Entry<String, JsonNode>> fields = dangerousGoodPatch.fields();
        while (fields.hasNext()) {
            final Map.Entry<String, JsonNode> field = fields.next();
            final String fieldName = "corosive".equals(field.getKey()) ? "corrosive" : field.getKey();
            merged.set(fieldName, field.getValue());
        }
        try {
            return objectMapper.treeToValue(merged, DangerousGoodApi.class);
        } catch (final JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid dangerous goods patch", exception);
        }
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
    public ResponseEntity<?> existsShipment(@PathVariable final Long shipmentId) {
        return ResponseEntity.status(HttpStatus.OK).body(shipmentPort.existsShipment(new ShipmentId(shipmentId)));
    }

    @PutMapping("/person")
    @Counted(value = "controller.person.update")
    @Timed(value = "controller.person.update")
    public ResponseEntity<?> updatePerson(@RequestBody final PersonApi personRequest,
                                          @RequestParam("shipmentId") final Long shipmentId,
                                          @RequestParam("personType") final PersonType personType) {
        final Person person = personType == PersonType.SENDER ? Sender.from(personRequest) : Recipient.from(personRequest);
        this.shipmentPort.changePersonTo(person, new ShipmentId(shipmentId));
        return ResponseEntity.status(HttpStatus.OK).body(new ShipmentResponseInformation(Status.OK));
    }
    
    @PutMapping("/shipment-type")
    @Counted(value = "controller.shipment.type.update")
    @Timed(value = "controller.shipment.type.update")
	public ResponseEntity<?> changeShipmentType(@RequestParam("shipmentType") final ShipmentType shipmentType,
                                                @RequestParam("shipmentId") final Long shipmentId) {
        final ChangeShipmentTypeRequest request = new ChangeShipmentTypeRequest(new ShipmentId(shipmentId), shipmentType);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleException(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ShipmentModificationException.class)
    public ResponseEntity<?> handleException(final ShipmentModificationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(DangerousGoodNotFoundException.class)
    public ResponseEntity<?> handleException(final DangerousGoodNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
