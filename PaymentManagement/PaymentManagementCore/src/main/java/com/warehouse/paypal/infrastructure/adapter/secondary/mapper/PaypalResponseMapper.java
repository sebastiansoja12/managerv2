package com.warehouse.paypal.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.warehouse.paypal.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.paypal.api.payments.Payment;


@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaypalResponseMapper {

    default PaypalResponse map(Payment payment) {
        final Links links = new Links();
		if (payment.getLinks() != null) {
			payment.getLinks().forEach(link -> {
				if (link.getRel().equals("approval_url")) {
					links.setHref(link.getHref());
					links.setRel(link.getRel());
				}
			});
		}
        return PaypalResponse.builder()
                .links(List.of(links))
                .createTime(payment.getCreateTime())
                .paymentMethod(payment.getPayer().getPaymentMethod())
                .failureReason(payment.getFailureReason())
                .transactions(map(payment.getTransactions()))
                .id(payment.getId())
                .state(payment.getState())
                .build();
    }

	default List<Transaction> map(List<com.paypal.api.payments.Transaction> transactions) {
		return transactions.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}

	default Transaction map(com.paypal.api.payments.Transaction transactions) {
		final Transaction transaction = new Transaction();
		transaction.setAmount(map(transactions.getAmount()));
		transaction.setDescription(transactions.getDescription());
		return transaction;
	}

	default Amount map(com.paypal.api.payments.Amount amount) {
		final Details details = Details.builder().subtotal(amount.getTotal()).build();
		return new Amount(amount.getTotal(), details);
	}

	default PaypalUpdateResponse mapToUpdateResponse(Payment payment) {
		return new PaypalUpdateResponse(payment.getState());
	}
}
