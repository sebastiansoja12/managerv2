package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.PaymentStatus;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaypalAdapter implements PaypalServicePort {

    private final PaypalPort paypalPort;

    private final PaymentMapper paymentMapper;

    @Override
    public PaymentStatus payment(Parcel parcel) {
        final PaymentRequest paymentRequest = buildPaymentRequest(parcel);
        final PaymentResponse paymentResponse = paypalPort.payment(paymentRequest);
        return paymentMapper.map(paymentResponse);
    }

    private PaymentRequest buildPaymentRequest(Parcel parcel) {
        return new PaymentRequest(parcel.getId(), parcel.getPrice());
    }
}
