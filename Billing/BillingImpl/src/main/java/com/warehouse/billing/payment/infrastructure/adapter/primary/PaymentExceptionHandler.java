package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentRetryApiResponse;
import com.warehouse.commonassets.exception.ProblemDetails;
import com.warehouse.commonassets.exception.ProblemDetailsException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.StaleStateException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(0)
@RestControllerAdvice(assignableTypes = {PaymentController.class, ExternalPaymentController.class,
        FakeGatewayController.class})
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ProblemDetails> handlePaymentException(final PaymentException exception,
                                                                 final HttpServletRequest request) {
        final int status = switch (exception.reason()) {
            case INVALID_REQUEST -> 400;
            case NOT_FOUND -> 404;
            case ILLEGAL_STATE -> 409;
            case GATEWAY_FAILURE -> 502;
        };
        final String title = switch (exception.reason()) {
            case INVALID_REQUEST -> "Invalid payment request";
            case NOT_FOUND -> "Payment data not found";
            case ILLEGAL_STATE -> "Payment cannot be changed";
            case GATEWAY_FAILURE -> "Payment gateway failure";
        };
        return problem(title, status, exception.getMessage(), request);
    }

    @ExceptionHandler(PaymentNotReadyException.class)
    public ResponseEntity<PaymentRetryApiResponse> handlePaymentNotReady(final PaymentNotReadyException exception) {
        return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON)
                .body(new PaymentRetryApiResponse(2));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetails> handleConcurrentPayment(final DataIntegrityViolationException exception,
                                                                  final HttpServletRequest request) {
        return problem("Payment changed concurrently", 409, "Payment was opened concurrently, retry the request",
                request);
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, OptimisticLockException.class,
            StaleStateException.class})
    public ResponseEntity<ProblemDetails> handleStalePayment(final RuntimeException exception,
                                                             final HttpServletRequest request) {
        return problem("Payment changed concurrently", 409, "Payment was changed by another request, reload it and retry",
                request);
    }

    private ResponseEntity<ProblemDetails> problem(final String title, final int status, final String detail,
                                                   final HttpServletRequest request) {
        final ProblemDetailsException problem = new ProblemDetailsException("about:blank", title, status, detail);
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProblemDetails.fromException(problem, request.getRequestURI()));
    }
}
