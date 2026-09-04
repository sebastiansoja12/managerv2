package com.warehouse.departmentmap.infrastructure.adapter.primary;

import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Order(0)
@RestControllerAdvice(assignableTypes = DepartmentMapController.class)
public class DepartmentMapExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DepartmentMapExceptionHandler.class);

    @ExceptionHandler(DepartmentMapException.class)
    public ResponseEntity<ProblemDetails> handleDepartmentMapException(final DepartmentMapException exception,
                                                                       final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND -> 404;
            case CONFLICT -> 409;
        };
        log.warn("Department map request rejected on {}: {} {}", request.getRequestURI(), exception.getCode(),
                exception.getMessage());
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank",
                "Department map request rejected", status, exception.getMessage(), null,
                Map.of("code", exception.getCode()));
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
