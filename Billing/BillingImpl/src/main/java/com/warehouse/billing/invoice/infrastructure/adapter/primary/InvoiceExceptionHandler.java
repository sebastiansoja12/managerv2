package com.warehouse.billing.invoice.infrastructure.adapter.primary;

import com.warehouse.billing.invoice.domain.exception.InvoiceException;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = InvoiceController.class)
public class InvoiceExceptionHandler {

    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<ProblemDetails> handleInvoiceException(final InvoiceException exception,
                                                                 final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND -> 404;
            case ILLEGAL_STATE -> 409;
        };
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", "Invoice request failed",
                status, exception.getMessage());
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
