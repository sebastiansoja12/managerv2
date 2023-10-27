package com.warehouse.paypal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentUpdateRequest;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalReadRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalRepositoryImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaymentNotFoundException;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;

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
        assertNotNull(savedEntity.getId());
    }

    @Test
    void shouldUpdatePayment() {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
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
                .amount(1)
                .paymentMethod("Paypal")
                .paymentUrl("paypal.url")
                .build();

        doReturn(Optional.of(entity))
                .when(readRepository)
                .findByPaymentId(updateRequest.getPaymentId());

        doReturn(savedEntity)
                .when(readRepository)
                .save(entity);
        // when
        repository.updatePayment(updateRequest);
        // then
        assertAll(
                () -> assertEquals(expected(PaymentStatus.PAID), entity.getPaymentStatus()),
                () -> assertEquals(expected(updateRequest.getPayerId()), entity.getPayerId())
        );
    }
    @Test
    void shouldNotUpdateWhenPaymentWasNotFound() {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaypalEntity entity = PaypalEntity.builder()
                .paymentId("paymentId")
                .paymentStatus(PaymentStatus.NOT_PAID)
                .amount(1)
                .paymentMethod("Paypal")
                .paymentUrl("paypal.url")
                .build();

        doThrow(new PaymentNotFoundException(404, "Payment not found"))
                .when(readRepository)
                .findByPaymentId(updateRequest.getPaymentId());
        // when
        final Executable executable = () -> repository.updatePayment(updateRequest);
        // then
        final PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, executable);
        assertAll(
                () -> assertEquals(expected(exception.getCode()), 404),
                () -> assertEquals(expected(exception.getMessage()), "Payment not found"),
                () -> assertEquals(expected(PaymentStatus.NOT_PAID), entity.getPaymentStatus()),
                () -> assertNotEquals((expected(updateRequest.getPayerId())), entity.getPayerId())
        );
    }

    private <T> T expected(T t) {
        return t;
    }

}
