package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;

import com.warehouse.paypal.domain.properties.PayeeProperties;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class PaypalServiceImpl implements PaypalService {

	private final PaypalServicePort paypalServicePort;

    private final PaypalRepository paypalRepository;

	private final RedirectUrls redirectUrls;

	private final static String HANDLING_FEE = "5";

	private final static String TAX = "23";

	private static final String MESSAGE_PAYMENT = "Payment for shipment: ";

    @Override
    public PaymentInformation payment(PaymentRequest paymentRequest, Payee payee) {
		final PaypalRequest request = buildPaypalRequest(paymentRequest, payee);
		final PaypalResponse paypalResponse = paypalServicePort.payment(request);
		return payment(paypalResponse, PaymentInformation.builder()
                .parcelId(paymentRequest.getParcelId())
                .price(paymentRequest.getPrice())
				.failureReason(paypalResponse.getFailureReason())
				.paymentMethod(paypalResponse.getPaymentMethod())
                .build());
    }

	@Override
    public String update(PaymentInformation paymentInformation) {
        return "";
    }

	private PaymentInformation payment(PaypalResponse response, PaymentInformation paymentInformation) {
		paymentInformation.setAmount(response.getTransactions().size());
		final List<Links> links = response.getLinks();
		if (links != null) {
			links.forEach(link -> {
				if (link.getRel() != null && link.getRel().equals("approval_url")) {
					paymentInformation.setPaymentUrl(link.getHref());
					paymentInformation.setPaymentStatus(PaymentStatus.NOT_PAID);
					paypalRepository.savePayment(paymentInformation);
				}
			});
		}
		return paymentInformation;
	}

    private PaypalRequest buildPaypalRequest(PaymentRequest payment, Payee payee) {
		final Details details = Details.builder()
				.subtotal(payment.getPrice().toString())
				.build();
        return PaypalRequest.builder()
				.intent("ORDER")
				.transaction(
						createTransaction(payment.getParcelId(), new Amount(payment.getPrice().toString(), details),
								payee))
				.payer(payment.getPayer())
				.redirectUrls(redirectUrls)
                .build();
    }

	private List<Transaction> createTransaction(Long parcelId, Amount amount, Payee payee) {
		final Transaction transaction = new Transaction();
		transaction.setDescription(MESSAGE_PAYMENT + parcelId);
		transaction.setAmount(amount);
		transaction.setPayee(payee);
		transaction.setTransactions(List.of(transaction));
		return List.of(transaction);
	}
}
