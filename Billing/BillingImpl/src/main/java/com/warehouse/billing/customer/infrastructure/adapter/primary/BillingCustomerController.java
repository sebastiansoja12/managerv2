package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.BillingCustomerApiKeyResponse;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.BillingCustomerApiResponse;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.CreateBillingCustomerApiRequest;
import com.warehouse.billing.customer.infrastructure.adapter.primary.api.UpdateBillingCustomerApiRequest;
import com.warehouse.billing.customer.infrastructure.adapter.primary.mapper.BillingCustomerWebMapper;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AccessUserControl
@RestController
@RequestMapping("/billing/customers")
public class BillingCustomerController {

    private final BillingCustomerPort billingCustomerPort;

    private final BillingCustomerWebMapper billingCustomerWebMapper;

    public BillingCustomerController(final BillingCustomerPort billingCustomerPort,
                                     final BillingCustomerWebMapper billingCustomerWebMapper) {
        this.billingCustomerPort = billingCustomerPort;
        this.billingCustomerWebMapper = billingCustomerWebMapper;
    }

    @GetMapping
    public ResponseEntity<List<BillingCustomerApiResponse>> findAll() {
        return ResponseEntity.ok(this.billingCustomerPort.findAll().stream()
                .map(this.billingCustomerWebMapper::toResponse)
                .toList());
    }

    @GetMapping("/{billingCustomerId}")
    public ResponseEntity<BillingCustomerApiResponse> get(@PathVariable final BillingCustomerId billingCustomerId) {
        return ResponseEntity.ok(this.billingCustomerWebMapper.toResponse(
                this.billingCustomerPort.get(billingCustomerId)));
    }

    @PostMapping
    public ResponseEntity<BillingCustomerApiResponse> create(
            @Valid @RequestBody final CreateBillingCustomerApiRequest request) {
        return ResponseEntity.ok(this.billingCustomerWebMapper.toResponse(
                this.billingCustomerPort.create(this.billingCustomerWebMapper.toCommand(request))));
    }

    @PutMapping("/{billingCustomerId}")
    public ResponseEntity<BillingCustomerApiResponse> update(
            @PathVariable final BillingCustomerId billingCustomerId,
            @Valid @RequestBody final UpdateBillingCustomerApiRequest request) {
        return ResponseEntity.ok(this.billingCustomerWebMapper.toResponse(this.billingCustomerPort.update(
                this.billingCustomerWebMapper.toCommand(billingCustomerId, request))));
    }

    @PostMapping("/{billingCustomerId}/api-key")
    public ResponseEntity<BillingCustomerApiKeyResponse> regenerateApiKey(
            @PathVariable final BillingCustomerId billingCustomerId) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(this.billingCustomerWebMapper.toApiKeyResponse(
                        this.billingCustomerPort.regenerateApiKey(billingCustomerId)));
    }

    @PostMapping("/{billingCustomerId}/block")
    public ResponseEntity<BillingCustomerApiResponse> block(@PathVariable final BillingCustomerId billingCustomerId) {
        return ResponseEntity.ok(this.billingCustomerWebMapper.toResponse(
                this.billingCustomerPort.block(billingCustomerId)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetails> handleConstraintViolation(final DataIntegrityViolationException exception,
                                                                    final HttpServletRequest request) {
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank",
                "Business customer already exists", HttpStatus.CONFLICT.value(),
                "Business customer with this tax id already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }

    @PostMapping("/{billingCustomerId}/unblock")
    public ResponseEntity<BillingCustomerApiResponse> unblock(
            @PathVariable final BillingCustomerId billingCustomerId) {
        return ResponseEntity.ok(this.billingCustomerWebMapper.toResponse(
                this.billingCustomerPort.unblock(billingCustomerId)));
    }
}
