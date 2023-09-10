package com.warehouse.paypal.infrastructure.adapter.secondary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.domain.properties.PayeeProperties;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaypalErrorException;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

@AllArgsConstructor
public class PaypalAdapter extends RestGatewaySupport implements PaypalServicePort {

    @NonNull
    private final APIContext apiContext;
    
    private final PaypalRequestMapper requestMapper;

    private final PaypalResponseMapper responseMapper;

    private final PayeeProperties payeeProperties = new PayeeProperties();

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

    private List<Transaction> transactions(Amount amount) {
        final Transaction transaction = new Transaction();
        transaction.setDescription("Payment for shipment");
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

    private Payee createPayee() {
        final Payee payee = new Payee();
        payee.setEmail(payeeProperties.getEmail());
        payee.setFirstName(payeeProperties.getFirstName());
        payee.setLastName(payeeProperties.getLastName());
        payee.setAccountNumber(payeeProperties.getAccountNumber());
        payee.setMerchantId(payeeProperties.getMerchantId());
        return payee;
    }
}
