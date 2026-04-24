package com.warehouse.exceptionhandler;

import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ProblemDetailsException.class)
    public ResponseEntity<ProblemDetails> handleProblemDetails(final ProblemDetailsException ex,
                                                               final HttpServletRequest request) {
        final ProblemDetails body = ProblemDetails.fromException(ex, request != null ? request.getRequestURI() : null);
        return ResponseEntity
                .status(ex.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleUnexpected(final Exception ex,
                                                           final HttpServletRequest request) {
        final ProblemDetailsException fallback = new ProblemDetailsException(
                "about:blank",
                "Internal Server Error",
                500,
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error");

        final ProblemDetails body = ProblemDetails.fromException(fallback, request != null ? request.getRequestURI() : null);
        return ResponseEntity
                .status(500)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body);
    }
}
