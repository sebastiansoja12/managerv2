package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaypalRequestMapper {

    default Payment map(PaypalRequest request) {
        final Payment payment = new Payment();
        payment.setIntent("ORDER");
        payment.setPayer(map(request.getPayer()));
        payment.setTransactions(map(request.getTransaction()));
        payment.setRedirectUrls(map(request.getRedirectUrls()));
        payment.setPayee(map(request.getPayee()));
        return payment;

    }

    com.paypal.api.payments.Payer map(Payer payer);

    List<com.paypal.api.payments.Transaction> map(List<Transaction> transactions);

    com.paypal.api.payments.Transaction map(Transaction transactions);

    com.paypal.api.payments.RedirectUrls map(RedirectUrls redirectUrls);

    com.paypal.api.payments.Payee map(Payee payee);
}
