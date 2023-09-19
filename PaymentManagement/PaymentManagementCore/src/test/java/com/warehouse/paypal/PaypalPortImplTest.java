package com.warehouse.paypal;


import com.warehouse.paypal.domain.enumeration.PaymentMethod;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.primary.PaypalPortImpl;
import com.warehouse.paypal.domain.service.PaypalService;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PaypalPortImplTest {

    public static final BigDecimal PRICE = new BigDecimal(1);
    public static final long PARCEL_ID = 1L;
    @Mock
    private PaypalService paypalService;

    private PaypalPortImpl paypalPort;

    @BeforeEach
    void setup() {
        paypalPort = new PaypalPortImpl(paypalService);
    }


    @Test
    void shouldCreatePayment() {
        // given
        final PaymentRequest request = new PaymentRequest(
                PARCEL_ID, PRICE, createPayer(), "ORDER"
        );
        final PaymentInformation information = PaymentInformation.builder()
                .amount(1)
                .price(PRICE)
                .parcelId(PARCEL_ID)
                .paymentStatus(PaymentStatus.NOT_PAID)
                .paymentMethod("Paypal")
                .paymentUrl("paypal.url")
                .build();

        doReturn(information)
                .when(paypalService)
                .payment(request);
        // when
        final PaymentResponse response = paypalPort.payment(request);
        // then
        assertAll(
                () -> assertEquals(expectedToBe("Paypal"), response.getPaymentMethod()),
                () -> assertEquals(expectedToBe("paypal.url"), response.getLink().getValue())
        );
    }

    @Test
    void shouldUpdatePayment() {
        // given
        final PaymentUpdateRequest request = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaymentUpdateResponse expectedResponse = PaymentUpdateResponse.builder()
                .status("OK")
                .build();

        doReturn(expectedResponse)
                .when(paypalService)
                .update(request);
        // when
        final PaymentUpdateResponse response = paypalPort.update(request);
        // then
        assertAll(
                () -> assertEquals(expectedToBe("OK"), response.getStatus())
        );
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

    private Payer createPayer() {
        return Payer.builder()
                .paymentMethod(PaymentMethod.PAYPAL)
                .payerInfo(createPayerInfo())
                .status("OK")
                .accountType("PAYPAL")
                .build();
    }

    private PayerInfo createPayerInfo() {
        return PayerInfo.builder()
                .phone("123")
                .email("sebastian5152@wp.pl")
                .firstName("Sebastian")
                .lastName("Soja")
                .build();
    }

}
