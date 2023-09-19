package com.warehouse.paypal.domain.service;

import java.util.List;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PaypalServiceImpl implements PaypalService {

	private final PaypalServicePort paypalServicePort;

    private final PaypalRepository paypalRepository;

	private final RedirectUrls redirectUrls;

	private static final String MESSAGE_PAYMENT = "Payment for shipment: ";

    @Override
    public PaymentInformation payment(PaymentRequest paymentRequest) {
		final PaypalRequest request = buildPaypalRequest(paymentRequest);
		final PaypalResponse paypalResponse = paypalServicePort.payment(request);
		return payment(paypalResponse, PaymentInformation.builder()
                .parcelId(paymentRequest.getParcelId())
                .price(paymentRequest.getPrice())
				.failureReason(paypalResponse.getFailureReason())
				.paymentMethod(paypalResponse.getPaymentMethod())
				.amount(paymentRequest.getPrice().intValue())
                .build());
    }

	@Override
	public PaymentUpdateResponse update(PaymentUpdateRequest paymentUpdateRequest) {
		final PaypalUpdateResponse response = paypalServicePort.update(paymentUpdateRequest);
		if (response.getState().equals("approved")) {
			paypalRepository.updatePayment(paymentUpdateRequest);
			return PaymentUpdateResponse.builder().status(response.getState()).build();
		}
		return PaymentUpdateResponse.builder()
				.status("NOT_OK")
				.build();
	}

	private PaymentInformation payment(PaypalResponse response, PaymentInformation paymentInformation) {
		final List<Links> links = response.getLinks();
		if (links != null) {
			links.forEach(link -> {
				if (link.getRel() != null && link.getRel().equals("approval_url")) {
					paymentInformation.setPaymentUrl(link.getHref());
					paymentInformation.setPaymentId(response.getId());
					paymentInformation.setPaymentStatus(PaymentStatus.NOT_PAID);
					paypalRepository.savePayment(paymentInformation);
				}
			});
		}
		return paymentInformation;
	}

    private PaypalRequest buildPaypalRequest(PaymentRequest paymentRequest) {
		final Details details = Details.builder()
				.subtotal(paymentRequest.getPrice().toString())
				.build();

        return PaypalRequest.builder()
				.intent(paymentRequest.getIntent())
				.transaction(createTransaction(paymentRequest.getParcelId(),
						new Amount(paymentRequest.getPrice().toString(), details)))
				.payer(paymentRequest.getPayer())
				.redirectUrls(redirectUrls)
                .build();
    }

	private List<Transaction> createTransaction(Long parcelId, Amount amount) {
		final Transaction transaction = new Transaction();
		transaction.setDescription(MESSAGE_PAYMENT + parcelId);
		transaction.setAmount(amount);
		return List.of(transaction);
	}
}
