package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.domain.enumeration.ParcelStatus;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.port.secondary.PaymentRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PaypalRepositoryImpl implements PaymentRepository {

    private final PaypalReadRepository readRepository;

    private final PaypalMapper mapper;

    @Override
    public void savePayment(PaymentInformation payment) {
        final PaypalEntity paypalEntity = mapper.map(payment);

        readRepository.save(paypalEntity);
    }

    @Override
    public void updatePayment(String paymentId) {
        final PaypalEntity entity = readRepository.findByPaymentId(paymentId);
        entity.setParcelStatus(ParcelStatus.PAID.toString());
        readRepository.save(entity);
    }

    @Override
    public PaymentInformation findByPaymentId(String paymentId) {

        final PaypalEntity paypalEntity = readRepository.findByPaymentId(paymentId);

        return mapper.map(paypalEntity);
    }
}
