package com.warehouse.paypal.infrastructure.adapter.secondary;

import java.util.List;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaypalErrorException;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PaypalAdapter implements PaypalServicePort {

    @NonNull
    private final APIContext apiContext;
    
    private final PaypalRequestMapper requestMapper;

    private final PaypalResponseMapper responseMapper;

    @Override
    public PaypalResponse payment(PaypalRequest paypalRequest) {
        final Payment response = requestMapper.map(paypalRequest);
        try {
            response.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new PaypalErrorException(0, "Error");
        }
        return responseMapper.map(response);
    }

    @Override
    public Payment update(String paymentId, String payerId) {
        Payment response = null;
        try {
            response = executePayment(paymentId, payerId);
            if (response.getState().equals("approved")) {
                //paypalRepository.updatePayment(paymentId);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return response;
    }

    private RedirectUrls getRedirectUrls() {
        final RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("CANCEL_URL");
        redirectUrls.setReturnUrl("SUCCESS_URL");
        return redirectUrls;
    }

    private Payer getPayer() {
        final Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        return payer;
    }

    private List<Transaction> transactions(Long parcelId, Amount amount) {
        final Transaction transaction = new Transaction();
        transaction.setDescription("Payment for shipment: " + parcelId);
        transaction.setAmount(amount);
        return List.of(transaction);
    }

    private Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        final Payment payment = new Payment();
        payment.setId(paymentId);
        final PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
