package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.Links;
import com.warehouse.paypal.domain.model.PaypalResponse;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaypalResponseMapper {

    default PaypalResponse map(Payment payment) {
        final Links links = new Links();
        for (com.paypal.api.payments.Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                link.setHref(link.getHref());
            }
        }
        return PaypalResponse.builder()
                .links(List.of(links))
                .createTime(payment.getCreateTime())
                .paymentMethod(payment.getPayer().getPaymentMethod())
                .failureReason(payment.getFailureReason())
                .build();
    }
}
