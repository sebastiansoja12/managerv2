package com.warehouse.paypal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    @Test
    @Disabled
    void shouldUpdatePayment() throws PayPalRESTException {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaypalUpdateResponse expectedResponse = new PaypalUpdateResponse("OK");

        final Payment mockedPayment = new Payment("ORDER", createPayer());
        mockedPayment.setId("id");

        final com.paypal.api.payments.PaymentExecution paymentExecution =
                mock(com.paypal.api.payments.PaymentExecution.class);

        // Mock the behavior of requestMapper
        when(requestMapper.map(updateRequest)).thenReturn(mockedPayment);

        mockApiContext();

        // Mock the behavior of executePayment
        when(mockedPayment.execute(apiContext, paymentExecution)).thenReturn(mockedPayment);

        // Mock the behavior of responseMapper
        when(responseMapper.mapToUpdateResponse(mockedPayment)).thenReturn(expectedResponse);

        // when
        final PaypalUpdateResponse result = paypalAdapter.update(updateRequest);

        // then
        verify(requestMapper).map(updateRequest);
        verify(mockedPayment).execute(apiContext, paymentExecution);
        verify(responseMapper).mapToUpdateResponse(mockedPayment);
        assertEquals("OK", result.getState());
    }


    private void mockApiContext() {
        when(apiContext.getAccessToken()).thenReturn("AccessToken");
        when(apiContext.getRequestId()).thenReturn("RequestId");
    }

    private com.paypal.api.payments.Payer createPayer() {
        final com.paypal.api.payments.Payer payer = new com.paypal.api.payments.Payer();
        payer.setPayerInfo(createPayerInfo());
        return payer;
    }

    private com.paypal.api.payments.PayerInfo createPayerInfo() {
        final com.paypal.api.payments.PayerInfo payerInfo = new com.paypal.api.payments.PayerInfo();
        payerInfo.setPayerId("payerId");
        return payerInfo;
    }


    private <T> T expectedToBe(T t) {
        return t;
    }

    private List<Transaction> createTransactions() {
        return List.of(Transaction.builder()
                .description("Payment for parcel")
                .amount(createAmount())
                .build());
    }

    private Amount createAmount() {
        return new Amount("30", createDetails());
    }

    private Details createDetails() {
        return Details.builder()
                .subtotal("30")
                .build();
    }

}
