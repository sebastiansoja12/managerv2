package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.CounterPaymentApiRequest;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentPageApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AccessUserControl
@RestController
@RequestMapping("/billing")
public class PaymentController {

    private final PaymentPort paymentPort;

    private final PaymentWebMapper paymentWebMapper;

    public PaymentController(final PaymentPort paymentPort, final PaymentWebMapper paymentWebMapper) {
        this.paymentPort = paymentPort;
        this.paymentWebMapper = paymentWebMapper;
    }

    @PostMapping("/quotes/{priceQuoteId}/counter-payment")
    public ResponseEntity<PaymentApiResponse> recordCounterPayment(
            @PathVariable final PriceQuoteId priceQuoteId,
            @Valid @RequestBody final CounterPaymentApiRequest request) {
        return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.recordCounterPayment(
                this.paymentWebMapper.toCommand(priceQuoteId, request))));
    }

    @GetMapping("/payments")
    public ResponseEntity<PaymentPageApiResponse> find(
            @RequestParam(required = false) final PaymentStatus status,
            @RequestParam(required = false) final PriceQuoteId priceQuoteId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.find(
                this.paymentWebMapper.toFindCommand(status, priceQuoteId, page, size))));
    }

    @PostMapping("/payments/{paymentId}/refund")
    public ResponseEntity<PaymentApiResponse> requestRefund(@PathVariable final PaymentId paymentId) {
        return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.requestManualRefund(paymentId)));
    }

    @PostMapping("/payments/{paymentId}/counter-refund")
    public ResponseEntity<PaymentApiResponse> confirmCounterRefund(@PathVariable final PaymentId paymentId) {
        return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.confirmCounterRefund(paymentId)));
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentApiResponse> get(@PathVariable final PaymentId paymentId) {
        return ResponseEntity.of(this.paymentPort.findById(paymentId).map(this.paymentWebMapper::toResponse));
    }
}
