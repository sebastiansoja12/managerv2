package com.warehouse.chat.infrastructure.adapter.primary;

import com.warehouse.chat.domain.exception.ChatException;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = OrganizationChatController.class)
public class OrganizationChatExceptionHandler {

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ProblemDetails> handleChatException(final ChatException exception,
                                                            final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case ACCESS_DENIED -> 403;
            case NOT_FOUND -> 404;
            case CONFLICT -> 409;
        };
        final String title = switch (exception.reason()) {
            case INVALID_REQUEST -> "Invalid chat request";
            case ACCESS_DENIED -> "Chat access denied";
            case NOT_FOUND -> "Chat resource not found";
            case CONFLICT -> "Chat message conflict";
        };
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status, exception.getMessage());
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
