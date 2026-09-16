package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ExternalShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ExternalShipmentCreateResponseApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.EmptyRequestException;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.TechnicalException;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/external/shipments")
public class ShipmentExternalController {

    private final ShipmentPort shipmentPort;

    private final ShipmentRequestValidator shipmentRequestValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentConfigurationPort shipmentConfigurationServicePort;

    private final OperatorContext operatorContext;

    public ShipmentExternalController(final ShipmentPort shipmentPort,
                                      final ShipmentRequestValidator shipmentRequestValidator,
                                      final ShipmentRequestMapper requestMapper,
                                      final ShipmentConfigurationPort shipmentConfigurationServicePort,
                                      final OperatorContext operatorContext) {
        this.shipmentPort = shipmentPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.requestMapper = requestMapper;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
        this.operatorContext = operatorContext;
    }

    @PostMapping
    @Counted(value = "controller.shipment.external.create")
    @Timed(value = "controller.shipment.external.create")
    public ResponseEntity<?> create(@RequestBody final ExternalShipmentCreateRequestApi externalShipmentRequest) {
        if (externalShipmentRequest.operatorId() == null) {
            return ResponseEntity.badRequest().body("Operator must be selected");
        }
        final OperatorId operatorId = new OperatorId(externalShipmentRequest.operatorId());
        if (!this.shipmentPort.isOperatorActive(operatorId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Operator is not available");
        }
        return this.operatorContext.runAs(operatorId, () -> create(externalShipmentRequest.shipment()));
    }

    private ResponseEntity<?> create(final ShipmentCreateRequestApi shipmentRequest) {
        final ShipmentValidationRules validationRules =
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration().validationRules();
        this.shipmentRequestValidator.validateRequest(shipmentRequest, validationRules);
        final ShipmentCreateCommand command = this.requestMapper.mapExternalCreateRequest(shipmentRequest);
        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(new ExternalShipmentCreateResponseApi(result.getSuccess().trackingNumber()));
        }
        return ResponseEntity.badRequest().body(result.getFailure().getMessage());
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
}
