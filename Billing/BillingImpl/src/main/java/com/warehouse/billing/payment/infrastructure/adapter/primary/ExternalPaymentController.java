package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.ExternalPaymentApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/payments")
public class ExternalPaymentController {

    private final PaymentPort paymentPort;

    private final BillingCustomerPort billingCustomerPort;

    private final PaymentWebMapper paymentWebMapper;

    public ExternalPaymentController(final PaymentPort paymentPort,
                                     final BillingCustomerPort billingCustomerPort,
                                     final PaymentWebMapper paymentWebMapper) {
        this.paymentPort = paymentPort;
        this.billingCustomerPort = billingCustomerPort;
        this.paymentWebMapper = paymentWebMapper;
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ExternalPaymentApiResponse> get(@PathVariable final String trackingNumber) {
        final BillingCustomer billingCustomer = this.billingCustomerPort.currentActive();
        return ResponseEntity.ok(this.paymentWebMapper.toResponse(this.paymentPort.openOnlineCheckout(
                this.paymentWebMapper.toTrackingNumber(trackingNumber), billingCustomer.id())));
    }
}
