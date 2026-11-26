package com.warehouse.analytics.infrastructure.adapter.primary;

import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = AnalyticsController.class)
public class AnalyticsExceptionHandler {

    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<ProblemDetails> handleAnalyticsException(final AnalyticsException exception,
                                                                   final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND -> 404;
        };
        final String title = switch (exception.reason()) {
            case INVALID_REQUEST -> "Invalid analytics request";
            case NOT_FOUND -> "Analytics resource not found";
        };
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status,
                exception.getMessage());
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
