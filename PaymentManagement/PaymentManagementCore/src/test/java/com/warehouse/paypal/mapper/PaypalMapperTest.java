package com.warehouse.paypal.mapper;


import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payee;
import com.warehouse.paypal.domain.enumeration.ParcelStatus;
import com.warehouse.paypal.domain.model.AmountInformation;
import com.warehouse.paypal.domain.model.PayeeInformation;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.infrastructure.adapter.secondary.entity.PaypalEntity;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PaypalMapperTest {

    private PaypalMapper paypalMapper;

    @BeforeEach
    void setUp() {
        paypalMapper = new PaypalMapperImpl();
    }

    @Test
    void shouldMapFromPaymentInformationToPaypalEntity() {
        // given
        final PaymentInformation information = PaymentInformation.builder()
                .amount(1)
                .parcelId(10001L)
                .paymentId("TEST")
                .paypalId("TEST.TEST")
                .parcelStatus(ParcelStatus.PAID)
                .payerId("1234")
                .paymentUrl("test.test")
                .build();
        // when
        final PaypalEntity entity = paypalMapper.map(information);
        // then
        assertThat(entity.getParcelStatus()).isEqualTo(information.getParcelStatus().toString());
        assertThat(entity.getPaymentUrl()).isEqualTo(information.getPaymentUrl());
    }

    @Test
    void shouldMapFromAmountInformationToAmount() {
        // given
        final AmountInformation amountInformation = AmountInformation.builder()
                .currency("PLN")
                .price(25)
                .total(1)
                .build();
        // when
        final Amount amount = paypalMapper.map(amountInformation);
        // then
        assertThat(amount.getCurrency()).isEqualTo(amountInformation.getCurrency());
    }

    @Test
    void shouldMapFromPaypalEntityToPaymentInformation() {
        // given
        final PaypalEntity paypalEntity = PaypalEntity.builder()
                .amount(1)
                .paymentId("123")
                .paymentUrl("test.test")
                .parcelStatus("NOT_PAID")
                .build();
        // when
        final PaymentInformation paymentInformation = paypalMapper.map(paypalEntity);
        // then
        assertThat(paymentInformation.getParcelStatus()).isEqualTo(ParcelStatus.NOT_PAID);
    }

    @Test
    void shouldMapFromPayeeInformationToPayee() {
        // given
        final PayeeInformation payeeInformation = new PayeeInformation();
        payeeInformation.setEmail("test@test.pl");
        payeeInformation.setFirstName("test");
        // when
        final Payee payee = paypalMapper.map(payeeInformation);
        // then
        assertThat(payee.getFirstName()).isEqualTo("test");
    }

}
