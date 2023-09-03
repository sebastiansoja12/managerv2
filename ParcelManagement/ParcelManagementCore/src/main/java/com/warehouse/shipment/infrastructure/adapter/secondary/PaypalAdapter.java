package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.paypal.domain.enumeration.PaymentMethod;
import com.warehouse.paypal.domain.model.Payer;
import com.warehouse.paypal.domain.model.PayerInfo;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.PaymentStatus;
import com.warehouse.shipment.domain.model.Sender;
import com.warehouse.shipment.domain.port.secondary.PaypalServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class PaypalAdapter implements PaypalServicePort {

    private final PaypalPort paypalPort;

    private final PaymentMapper paymentMapper;

    private final String INTENT = "ORDER";

    @Override
    public PaymentStatus payment(Parcel parcel) {
		final PaymentRequest paymentRequest = new PaymentRequest(parcel.getId(), BigDecimal.valueOf(parcel.getPrice()),
				createPayer(parcel.getSender()), INTENT);
        final PaymentResponse paymentResponse = paypalPort.payment(paymentRequest);
        return paymentMapper.map(paymentResponse);
    }

    private Payer createPayer(Sender sender) {
        return Payer.builder()
                .paymentMethod(PaymentMethod.PAYPAL)
                .payerInfo(createPayerInfo(sender))
                .build();
    }

    private PayerInfo createPayerInfo(Sender sender) {
        return PayerInfo.builder()
                .firstName(sender.getFirstName())
                .lastName(sender.getLastName())
                .email(sender.getEmail())
                .phone(sender.getTelephoneNumber())
                .build();
    }
}
