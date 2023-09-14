package com.warehouse.paypal.infrastructure.adapter.secondary;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PaypalRepositoryImpl implements PaypalRepository {

    private final PaypalReadRepository readRepository;

    private final PaypalMapper mapper;

    @Override
    public void savePayment(PaymentInformation payment) {
        final PaypalEntity paypalEntity = mapper.map(payment);
        readRepository.save(paypalEntity);
    }

    @Override
    public void updatePayment(Long paymentId) {
        final PaypalEntity entity = null;
        entity.setPaymentStatus(PaymentStatus.PAID);
        readRepository.save(entity);
    }

    @Override
    public PaymentInformation findByPaymentId(Long paymentId) {
        final PaypalEntity paypalEntity = null;
        return mapper.map(paypalEntity);
    }
}
