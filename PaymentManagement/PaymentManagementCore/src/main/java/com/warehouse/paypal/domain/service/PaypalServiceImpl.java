package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;

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
    public PaymentInformation payment(Payment payment) {
		final PaypalRequest request = buildPaypalRequest(payment);
		final PaypalResponse paypalResponse = paypalServicePort.payment(request);
		return payment(paypalResponse, PaymentInformation.builder()
                .parcelId(payment.getParcelId())
                .price(payment.getPrice())
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
		for (Links link : response.getLinks()) {
			if (link.getRel().equals("approval_url")) {
				paymentInformation.setPaymentUrl(link.getHref());
				paymentInformation.setPaymentStatus(PaymentStatus.NOT_PAID);
				paypalRepository.savePayment(paymentInformation);
			}
		}
		return paymentInformation;
	}

    private PaypalRequest buildPaypalRequest(Payment payment) {
		final Details details = Details.builder()
				.tax(TAX)
				.handlingFee(HANDLING_FEE)
				.subtotal(payment.getPrice().toString())
				.build();
        return PaypalRequest.builder()
				.transaction(
						createTransaction(payment.getParcelId(), new Amount(payment.getPrice().toString(), details)))
				.payer(new Payer())
				.redirectUrls(redirectUrls)
                .build();
    }

	private List<Transaction> createTransaction(Long parcelId, Amount amount) {
		final Transaction transaction = new Transaction();
		transaction.setDescription(MESSAGE_PAYMENT + parcelId);
		transaction.setAmount(amount);
		transaction.setTransactions(List.of(transaction));
		return List.of(transaction);
	}

}
