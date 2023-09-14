package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaypalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.support.RestGatewaySupport;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.domain.properties.PaypalConfigurationProperties;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PaypalAdapter extends RestGatewaySupport implements PaypalServicePort {

    
    private final PaypalRequestMapper requestMapper;

    private final PaypalResponseMapper responseMapper;

    @NonNull
    private final PaypalConfigurationProperties paypalProperties;
    private final Logger logger = LoggerFactory.getLogger(PaypalAdapter.class);

    private final APIContext apiContext;


    @Override
    public PaypalResponse payment(PaypalRequest paypalRequest) {
        final Payment payment = requestMapper.map(paypalRequest);
        final Payment response;
        try {
            response = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            logger.info("Exception during payment for parcel {} with payment method {}", paypalRequest.getParcelId(),
                    paypalRequest.getPayer().getPaymentMethod());
            throw new PaypalErrorException(0, e.getMessage());
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

    private Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        final Payment payment = new Payment();
        payment.setId(paymentId);
        final PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return null;
    }
}
