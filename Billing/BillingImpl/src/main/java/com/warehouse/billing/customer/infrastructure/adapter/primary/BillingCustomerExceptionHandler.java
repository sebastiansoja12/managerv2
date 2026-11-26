package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.ExternalPaymentController;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = {BillingCustomerController.class, ExternalPaymentController.class})
public class BillingCustomerExceptionHandler {

    @ExceptionHandler(BillingCustomerException.class)
    public ResponseEntity<ProblemDetails> handleBillingCustomerException(final BillingCustomerException exception,
                                                                         final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case NOT_FOUND -> 404;
            case DUPLICATE -> 409;
            case NOT_ACTIVE -> 403;
        };
        final String title = switch (exception.reason()) {
            case NOT_FOUND -> "Business customer not found";
            case DUPLICATE -> "Business customer already exists";
            case NOT_ACTIVE -> "Business customer is not active";
        };
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status,
                exception.getMessage());
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
