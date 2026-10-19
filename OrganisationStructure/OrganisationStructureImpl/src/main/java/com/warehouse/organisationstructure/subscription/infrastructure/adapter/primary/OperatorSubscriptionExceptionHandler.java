package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.organisationstructure.subscription.domain.exception.InvalidSubscriptionException;
import com.warehouse.organisationstructure.subscription.domain.exception.OperatorNotFoundException;
import com.warehouse.organisationstructure.subscription.domain.exception.PlatformOwnerRequiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@Order(0)
@RestControllerAdvice(assignableTypes = {OperatorSubscriptionController.class, CurrentSubscriptionController.class})
public class OperatorSubscriptionExceptionHandler {

    @ExceptionHandler(InvalidSubscriptionException.class)
    public ResponseEntity<ProblemDetails> handleInvalidSubscription(final InvalidSubscriptionException exception,
                                                                    final HttpServletRequest request) {
        return problem(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetails> handleInvalidOperatorId(final MethodArgumentTypeMismatchException exception,
                                                                  final HttpServletRequest request) {
        return problem(HttpStatus.BAD_REQUEST, "Operator id must be a positive number", request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ProblemDetails> handleInvalidRequest(final Exception exception,
                                                               final HttpServletRequest request) {
        return problem(HttpStatus.BAD_REQUEST,
                "Subscription plan, start date and end date are required in ISO format (yyyy-MM-dd)", request);
    }

    @ExceptionHandler(OperatorNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleOperatorNotFound(final OperatorNotFoundException exception,
                                                                 final HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(PlatformOwnerRequiredException.class)
    public ResponseEntity<ProblemDetails> handlePlatformOwnerRequired(final PlatformOwnerRequiredException exception,
                                                                      final HttpServletRequest request) {
        return problem(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }

    private ResponseEntity<ProblemDetails> problem(final HttpStatus status,
                                                   final String detail,
                                                   final HttpServletRequest request) {
        log.warn("Operator subscription request rejected on {}: {}", request.getRequestURI(), detail);
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank",
                "Operator subscription request rejected", status.value(), detail);
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
