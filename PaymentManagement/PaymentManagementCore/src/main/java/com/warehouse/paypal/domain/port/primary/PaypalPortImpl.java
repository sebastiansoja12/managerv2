package com.warehouse.paypal.domain.port.primary;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.properties.PayeeProperties;
import com.warehouse.paypal.domain.service.PaypalService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaypalPortImpl implements PaypalPort {

    private final PaypalService paypalService;

    private final PayeeProperties payeeProperties;

    @Override
    public PaymentResponse payment(PaymentRequest request) {
        final Payee payee = createPayee();
        final PaymentInformation paymentInformation = paypalService.payment(request, payee);
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
    
    private Payee createPayee() {
        return new Payee(
                "sebastian5152@wp.pl",
                "2283821193",
                "Sebastian",
                "Soja",
                "47544077932802225870952690"
        );
    }
    
}
