package com.warehouse.paypal.infrastructure.adapter.secondary;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentMethod;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaypalMockAdapter implements PaypalServicePort {

	private final static String CONSTANT_PREFIX = "PAYID-MUM%s";
	@Override
	public PaypalUpdateResponse update(PaymentUpdateRequest updateRequest) {
		if (StringUtils.isNotEmpty(updateRequest.getPayerId())
				&& StringUtils.isNotEmpty(updateRequest.getPaymentId())) {
			return new PaypalUpdateResponse("approved");
		}
		return new PaypalUpdateResponse("not_approved");
	}

    @Override
    public PaypalResponse payment(PaypalRequest paypalRequest) {

		final String id = String.format(CONSTANT_PREFIX, UUID.randomUUID());
		final String url = "http://localhost:8080/v2/api/payments/success?paymentId=%s&PayerID=1";

        final List<Links> linksList = List.of(Links.builder()
                .href(String.format(url, id))
                .rel("approval_url")
                .build());
        
        return PaypalResponse.builder()
                .intent(paypalRequest.getIntent())
                .payer(paypalRequest.getPayer())
                .transactions(paypalRequest.getTransaction())
                .id(id)
                .links(linksList)
                .paymentMethod(PaymentMethod.PAYPAL.name())
                .state("OK")
                .createTime(LocalDateTime.now().toString())
                .build();
    }
}
