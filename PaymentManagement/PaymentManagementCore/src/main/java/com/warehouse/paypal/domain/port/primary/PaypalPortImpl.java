package com.warehouse.paypal.domain.port.primary;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.service.PaypalService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaypalPortImpl implements PaypalPort {

    private final PaypalService paypalService;

    @Override
    public PaymentResponse payment(PaymentRequest request) {
        final Payment payment = buildPayment(request);
        final PaymentInformation paymentInformation = paypalService.payment(payment);
        return PaymentResponse.builder()
                .createTime(paymentInformation.getCreateTime())
                .paymentMethod(paymentInformation.getPaymentMethod())
                .link(new Link(paymentInformation.getPaymentUrl()))
                .failureReason(paymentInformation.getFailureReason())
                .build();
    }

    public String update(PaymentInformation paymentInformation) {
        return paypalService.update(paymentInformation);
    }

    private Payment buildPayment(PaymentRequest request) {
        return Payment.builder()
                .price(request.getPrice())
                .parcelId(request.getParcelId())
                .build();
    }
}
