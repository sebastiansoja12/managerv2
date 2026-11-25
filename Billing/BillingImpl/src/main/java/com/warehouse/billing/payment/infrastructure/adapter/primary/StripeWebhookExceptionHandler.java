package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.exception.InvalidWebhookPayloadException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.exception.InvalidWebhookSignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(0)
@RestControllerAdvice(assignableTypes = StripeWebhookController.class)
public class StripeWebhookExceptionHandler {

    @ExceptionHandler(InvalidWebhookSignatureException.class)
    public ResponseEntity<Void> handleInvalidSignature(final InvalidWebhookSignatureException exception) {
        log.warn("Stripe webhook rejected: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(InvalidWebhookPayloadException.class)
    public ResponseEntity<Void> handleInvalidPayload(final InvalidWebhookPayloadException exception) {
        log.warn("Stripe webhook rejected: {}", exception.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PaymentNotReadyException.class)
    public ResponseEntity<Void> handleNotReadyPayment(final PaymentNotReadyException exception) {
        log.warn("Stripe webhook arrived before the payment change was recorded and will be retried: {}",
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Void> handleUnprocessablePayment(final PaymentException exception) {
        if (PaymentException.Reason.GATEWAY_FAILURE.equals(exception.reason())) {
            log.warn("Stripe webhook failed and will be retried: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        log.warn("Stripe webhook acknowledged without payment change: {}", exception.getMessage());
        return ResponseEntity.ok().build();
    }
}
