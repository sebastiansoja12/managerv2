package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.properties.PayeeProperties;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaypalRequestMapper {

    default Payment map(PaypalRequest request) {
        final Payment payment = new Payment();
        payment.setIntent("order");
        payment.setPayer(map(request.getPayer()));
        payment.setTransactions(map(request.getTransaction()));
        payment.setRedirectUrls(map(request.getRedirectUrls()));
        return payment;

    }

    default com.paypal.api.payments.Payer map(Payer payer) {
        final com.paypal.api.payments.Payer paypalPayer = new com.paypal.api.payments.Payer();
        paypalPayer.setPaymentMethod(payer.getPaymentMethod().toString());
        return paypalPayer;
    }
    
    com.paypal.api.payments.PayerInfo map(PayerInfo payerInfo);
    
    default List<com.paypal.api.payments.Transaction> map(List<Transaction> transactions) {
		return transactions.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    default com.paypal.api.payments.Transaction map(Transaction transactions) {
        final com.paypal.api.payments.Transaction transaction = new com.paypal.api.payments.Transaction();
        transaction.setAmount(map(transactions.getAmount()));
        transaction.setDescription(transactions.getDescription());
        return transaction;
    }

    com.paypal.api.payments.RedirectUrls map(RedirectUrls redirectUrls);

    com.paypal.api.payments.Payee map(Payee payee);

    default com.paypal.api.payments.Amount map(Amount amount) {
        final com.paypal.api.payments.Amount paypalAmount = new com.paypal.api.payments.Amount();
        paypalAmount.setTotal(amount.getTotal());
        paypalAmount.setCurrency(amount.getCurrency().name());
        paypalAmount.setDetails(map(amount.getDetails()));
        return paypalAmount;
    }

    default com.paypal.api.payments.Details map(Details details) {
        final com.paypal.api.payments.Details d = new com.paypal.api.payments.Details();
        d.setSubtotal(details.getSubtotal());
        return d;
    }
}
