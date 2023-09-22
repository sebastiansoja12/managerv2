package com.warehouse.paypal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.enumeration.PaymentMethod;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalAdapter;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaypalErrorException;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

@ExtendWith(MockitoExtension.class)
public class PaypalAdapterTest {


    @Mock
    private PaypalRequestMapper requestMapper;

    @Mock
    private PaypalResponseMapper responseMapper;

    @Mock
    private APIContext apiContext;

    private PaypalAdapter paypalAdapter;


    @BeforeEach
    void setup() {
        paypalAdapter = new PaypalAdapter(requestMapper, responseMapper, apiContext);
    }

    @Test
    void shouldCreatePayment() throws PayPalRESTException {
        // given
        final PaypalRequest paypalRequest = PaypalRequest.builder()
                .intent("ORDER")
                .parcelId(1L)
                .redirectUrls(mock(RedirectUrls.class))
                .transaction(createTransactions())
                .build();

        final Payment mockedPayment = mock(Payment.class);
        when(requestMapper.map(paypalRequest)).thenReturn(mockedPayment);

        final Payment responsePayment = new Payment();
        when(mockedPayment.create(apiContext)).thenReturn(responsePayment);

        final PaypalResponse expectedResponse = PaypalResponse.builder()
                .intent("ORDER")
                .build();

        when(responseMapper.map(responsePayment)).thenReturn(expectedResponse);
        // when
        final PaypalResponse response = paypalAdapter.payment(paypalRequest);
        // then
        assertEquals(expectedToBe("ORDER"), response.getIntent());
    }

    @Test
    void shouldNotCreatePayment() throws PayPalRESTException {
        // given
        final PaypalRequest paypalRequest = PaypalRequest.builder()
                .intent("ORDER")
                .parcelId(1L)
                .payer(Payer.builder()
                        .paymentMethod(PaymentMethod.PAYPAL)
                        .build()
                )
                .redirectUrls(mock(RedirectUrls.class))
                .transaction(createTransactions())
                .build();

        final Payment mockedPayment = mock(Payment.class);
        when(requestMapper.map(paypalRequest)).thenReturn(mockedPayment);
        when(mockedPayment.create(apiContext)).thenThrow(new PayPalRESTException("Payment error"));

        // when
        final Executable executable = () -> paypalAdapter.payment(paypalRequest);

        // then
        final PaypalErrorException exception = assertThrows(PaypalErrorException.class, executable);
        assertEquals(0, exception.getCode());
        assertEquals("Payment error", exception.getMessage());
    }

    private <T> T expectedToBe(T t) {
        return t;
    }

    private List<Transaction> createTransactions() {
        return List.of(Transaction.builder()
                .description("Payment for parcel")
                .build());
    }
}
