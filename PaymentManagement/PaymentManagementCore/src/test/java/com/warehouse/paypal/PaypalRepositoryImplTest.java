package com.warehouse.paypal;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalReadRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalRepositoryImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaypalRepositoryImplTest {

    @Mock
    private PaypalReadRepository readRepository;

    private PaypalMapper paypalMapper = Mappers.getMapper(PaypalMapper.class);

    private PaypalRepositoryImpl repository;


    @BeforeEach
    void setup() {
        repository = new PaypalRepositoryImpl(readRepository, paypalMapper);
    }

    @Test
    void shouldSavePayment() {
        // given
        final PaymentInformation information = PaymentInformation.builder()
                .amount(1)
                .price(new BigDecimal(30))
                .paymentStatus(PaymentStatus.NOT_PAID)
                .paymentMethod("Paypal")
                .paypalId("paypalId")
                .paymentId("paymentId")
                .payerId("payerId")
                .paymentUrl("paypal.url")
                .build();

        final PaypalEntity entity = PaypalEntity.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .paymentStatus(PaymentStatus.NOT_PAID)
                .amount(1)
                .paymentMethod("Paypal")
                .paymentUrl("paypal.url")
                .build();

        final PaypalEntity savedEntity = PaypalEntity.builder()
                .id(1L)
                .paymentId("paymentId")
                .paymentStatus(PaymentStatus.NOT_PAID)
                .payerId("payerId")
                .amount(1)
                .paymentMethod("Paypal")
                .paymentUrl("paypal.url")
                .build();

        doReturn(savedEntity)
                .when(readRepository)
                .save(entity);
        // when
        repository.savePayment(information);
        // then
        assertNotNull(entity.getId());
    }

    @Test
    void shouldUpdatePayment() {
        // given

        // when

        // then
    }

    @Test
    void shouldNotUpdateWhenPaymentWasNotFound() {
        // given

        // when

        // then
    }
}
