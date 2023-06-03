package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.enumeration.ParcelStatus;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaymentRepository;
import com.warehouse.paypal.domain.port.secondary.PaymentSecondaryPort;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PaypalAdapter implements PaymentSecondaryPort {

    private final APIContext apiContext;

    private final static String SUCCESS_URL = "/v2/api/payments/pay/success";
    
    private final static String CANCEL_URL = "/v2/api/payments/pay/cancel";
    
    private final PaypalMapper paypalMapper;

    private final PaypalResponseMapper responseMapper;

    private final PaymentRepository paymentRepository;
    
    private final static String CURRENCY = "PLN";

    @Override
    public PaymentResponse payment(PaymentRequest request) {
        final PaymentInformation paymentInformation = PaymentInformation.builder().build();
        final Payment payment = createPayment(request);

        paymentInformation.setParcelId(request.getParcelId());
        paymentInformation.setParcelStatus(ParcelStatus.NOT_PAID);
        paymentInformation.setPaypalId(payment.getId());
        paymentInformation.setPaymentId(payment.getId());

        obtainPaymentUrl(payment, paymentInformation);

        return responseMapper.map(payment);
    }

    @Override
    public String update(String paymentId, String payerId) {
        String response = null;
        try {
            response = successPay(paymentId, payerId);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Payment createPayment(PaymentRequest paymentRequest) {
        
        final AmountInformation amountInformation = AmountInformation.builder()
                .currency(CURRENCY)
                .price(paymentRequest.getPrice())
                .total(paymentRequest.getPrice())
                .build();
        
        final Amount amount = paypalMapper.map(amountInformation);

        final List<Transaction> transactions = transactions(paymentRequest.getParcelId(), amount);

        final Payer payer = getPayer();

        final RedirectUrls redirectUrls = getRedirectUrls();

        final Payment payment = getPayment(redirectUrls, transactions, payer);

        try {
            return payment.create(apiContext);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Payment getPayment(RedirectUrls redirectUrls, List<Transaction> transactions, Payer payer) {
        final Payment payment = new Payment();
        payment.setIntent("ORDER");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    private RedirectUrls getRedirectUrls() {
        final RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_URL);
        redirectUrls.setReturnUrl(SUCCESS_URL);
        return redirectUrls;
    }

    private Payer getPayer() {
        final Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        return payer;
    }

    public List<Transaction> transactions(Long parcelId, Amount amount) {
        final Transaction transaction = new Transaction();
        transaction.setDescription("Payment for shipment: " + parcelId);
        transaction.setAmount(amount);

        final List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        
        return transactions;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        final Payment payment = new Payment();
        payment.setId(paymentId);
        final PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }



    public String successPay(String paymentId, String payerId) throws PayPalRESTException {
        final Payment payment = executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            paymentRepository.updatePayment(paymentId);
            return "Payment successful";
        }
        return "Payment not successful";
    }

    private void obtainPaymentUrl(Payment payment, PaymentInformation paymentInformation) {
        paymentInformation.setAmount(payment.getTransactions().size());
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                paymentInformation.setPaymentUrl(link.getHref());
                paymentInformation.setParcelStatus(ParcelStatus.NOT_PAID);
                paymentRepository.savePayment(paymentInformation);
            }
        }
    }

}
