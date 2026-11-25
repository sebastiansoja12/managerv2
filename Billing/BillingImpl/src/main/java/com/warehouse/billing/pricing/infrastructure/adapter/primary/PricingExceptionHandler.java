package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = PricingController.class)
public class PricingExceptionHandler {

    @ExceptionHandler(PriceListException.class)
    public ResponseEntity<ProblemDetails> handlePriceListException(final PriceListException exception,
                                                                   final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND -> 404;
        };
        final String title = switch (exception.reason()) {
            case INVALID_REQUEST -> "Invalid price list";
            case NOT_FOUND -> "Price list not found";
        };
        return problem(title, status, exception.getMessage(), request);
    }

    @ExceptionHandler(ShipmentNotPriceableException.class)
    public ResponseEntity<ProblemDetails> handleShipmentNotPriceable(final ShipmentNotPriceableException exception,
                                                                     final HttpServletRequest request) {
        return problem("Shipment cannot be priced", 422, exception.getMessage(), request);
    }

    private ResponseEntity<ProblemDetails> problem(final String title, final int status, final String detail,
                                                   final HttpServletRequest request) {
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status, detail);
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
