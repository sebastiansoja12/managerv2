package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;

import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.Links;
import com.warehouse.paypal.domain.model.PaypalResponse;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.CollectionUtils;


@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaypalResponseMapper {

    default PaypalResponse map(Payment payment) {
        final Links links = new Links();

        if (payment.getLinks() != null) {
            payment.getLinks()
                    .forEach(link -> {
                        if (link.getRel().equals("approval_url")) {
                            links.setHref(link.getHref());
                        }
                    });
        }
        return PaypalResponse.builder()
                .links(List.of(links))
                .createTime(payment.getCreateTime())
                .paymentMethod(payment.getPayer().getPaymentMethod())
                .failureReason(payment.getFailureReason())
                .transactions(payment.getTransactions())
                .build();
    }
}
