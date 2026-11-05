package com.warehouse.incident.infrastructure.adapter.primary;

import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.incident.domain.exception.IncidentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = IncidentController.class)
public class IncidentExceptionHandler {

    @ExceptionHandler(IncidentException.class)
    public ResponseEntity<ProblemDetails> handleIncidentException(final IncidentException exception,
                                                                  final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case ACCESS_DENIED -> 403;
            case NOT_FOUND -> 404;
            case CONFLICT -> 409;
        };
        final String title = switch (exception.reason()) {
            case INVALID_REQUEST -> "Invalid incident request";
            case ACCESS_DENIED -> "Incident access denied";
            case NOT_FOUND -> "Incident not found";
            case CONFLICT -> "Incident state conflict";
        };
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status,
                exception.getMessage());
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
