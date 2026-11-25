package com.warehouse.shipment.infrastructure.adapter.primary;

import java.time.Clock;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.security.AttemptLimiter;
import com.warehouse.commonassets.security.InMemoryAttemptLimiter;
import com.warehouse.shipment.application.port.primary.ShipmentCustomerPort;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.domain.exception.BillingCustomerNotActiveException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ExternalShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ExternalShipmentCreateResponseApi;
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

    private static final String PAYMENT_PATH = "/external/payments/";

    private final ShipmentPort shipmentPort;

    private final ShipmentCustomerPort shipmentCustomerPort;

    private final ShipmentRequestValidator shipmentRequestValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentConfigurationPort shipmentConfigurationServicePort;

    private final AttemptLimiter customerRequestLimiter;

    public ShipmentExternalController(final ShipmentPort shipmentPort,
                                      final ShipmentCustomerPort shipmentCustomerPort,
                                      final ShipmentRequestValidator shipmentRequestValidator,
                                      final ShipmentRequestMapper requestMapper,
                                      final ShipmentConfigurationPort shipmentConfigurationServicePort,
                                      @Value("${shipment.external.max-requests-per-client:30}")
                                      final int maxRequestsPerCustomer,
                                      @Value("${shipment.external.rate-window:PT10M}") final Duration rateWindow) {
        this.shipmentPort = shipmentPort;
        this.shipmentCustomerPort = shipmentCustomerPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.requestMapper = requestMapper;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
        this.customerRequestLimiter = new InMemoryAttemptLimiter(maxRequestsPerCustomer, rateWindow,
                Clock.systemUTC());
    }

    @PostMapping
    @Counted(value = "controller.shipment.external.create")
    @Timed(value = "controller.shipment.external.create")
    public ResponseEntity<?> create(@RequestBody final ExternalShipmentCreateRequestApi externalShipmentRequest) {
        final BillingCustomerId billingCustomerId = this.shipmentCustomerPort.currentActiveCustomer();
        final String limiterKey = String.valueOf(billingCustomerId.value());
        if (this.customerRequestLimiter.isBlocked(limiterKey)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Try again later");
        }
        this.customerRequestLimiter.recordAttempt(limiterKey);
        final ShipmentValidationRules validationRules =
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration().validationRules();
        this.shipmentRequestValidator.validateRequest(externalShipmentRequest.shipment(), validationRules);
        final ShipmentCreateCommand command = this.requestMapper.mapExternalCreateRequest(
                externalShipmentRequest.shipment());
        command.setBillingCustomerId(billingCustomerId);
        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(command);

        if (result.isSuccess()) {
            final ShipmentCreateResponse response = result.getSuccess();
            return ResponseEntity.ok().body(new ExternalShipmentCreateResponseApi(response.trackingNumber(),
                    response.paymentRequired() ? PAYMENT_PATH + response.trackingNumber() : null));
        }
        return ResponseEntity.badRequest().body(result.getFailure().getMessage());
    }

    @ExceptionHandler(BillingCustomerNotActiveException.class)
    public ResponseEntity<?> handleException(final BillingCustomerNotActiveException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
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
