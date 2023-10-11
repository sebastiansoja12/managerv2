package com.warehouse.paypal.infrastructure.adapter.secondary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.support.RestGatewaySupport;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.model.PaymentUpdateRequest;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;
import com.warehouse.paypal.domain.model.PaypalUpdateResponse;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaypalErrorException;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaypalAdapter extends RestGatewaySupport implements PaypalServicePort {

    
    private final PaypalRequestMapper requestMapper;

    private final PaypalResponseMapper responseMapper;

    private final APIContext apiContext;

    private final Logger logger = LoggerFactory.getLogger(PaypalAdapter.class);

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
    public PaypalUpdateResponse update(PaymentUpdateRequest updateRequest) {
        final Payment payment = requestMapper.map(updateRequest);
        final Payment response;
        try {
            response = executePayment(payment);
        } catch (PayPalRESTException e) {
            logger.info("Error while executing payment: {}", e.getMessage());
            throw new PaypalErrorException(0, e.getMessage());
        }
        return responseMapper.mapToUpdateResponse(response);
    }

    private Payment executePayment(Payment payment) throws PayPalRESTException {
        final PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
        return payment.execute(apiContext, paymentExecute);
    }
}
