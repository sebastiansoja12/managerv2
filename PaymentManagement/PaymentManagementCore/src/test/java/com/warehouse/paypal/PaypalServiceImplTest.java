package com.warehouse.paypal;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.paypal.domain.enumeration.PaymentMethod;
import com.warehouse.paypal.domain.model.*;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.domain.service.PaypalServiceImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.enumeration.PaymentStatus;
import com.warehouse.paypal.infrastructure.adapter.secondary.exception.PaymentNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PaypalServiceImplTest {

    @Mock
    private PaypalServicePort paypalServicePort;

    @Mock
    private PaypalRepository paypalRepository;

    @Mock
    private RedirectUrls redirectUrls;

    private PaypalServiceImpl service;

    @BeforeEach
    void setup() {
        service = new PaypalServiceImpl(paypalServicePort, paypalRepository, redirectUrls);
    }

    @Test
    void shouldCreatePayment() {
        // given
        final PaymentRequest request = new PaymentRequest(
                1L, new BigDecimal(30), createPayer(),
                "ORDER"
        );
        final PaypalRequest paypalRequest = buildPaypalRequest(request);
        final PaypalResponse paypalResponse = PaypalResponse.builder()
                .state("OK")
                .id("id")
                .links(createLinks())
                .paymentMethod("PAYPAL")
                .build();
        doReturn(paypalResponse)
                .when(paypalServicePort)
                .payment(paypalRequest);
        // when
        final PaymentInformation information = service.payment(request);
        // then
        assertAll(
                () -> assertEquals(expected(PaymentStatus.NOT_PAID), information.getPaymentStatus()),
                () -> assertEquals(expected("href"), information.getPaymentUrl())

        );
    }

    @Test
    void shouldNotCreatePaymentWhenRelUrlIsNotApprove() {
        // given
        final PaymentRequest request = new PaymentRequest(
                1L, new BigDecimal(30), createPayer(),
                "ORDER"
        );
        final PaypalRequest paypalRequest = buildPaypalRequest(request);

        // create mock Links with Rel different than approve_url
        final List<Links> links = List.of(Links.builder()
                .href("href")
                .rel("error")
                .build());

        final PaypalResponse paypalResponse = PaypalResponse.builder()
                .state("ORDER")
                .id("id")
                .links(links)
                .paymentMethod("PAYPAL")
                .build();
        doReturn(paypalResponse)
                .when(paypalServicePort)
                .payment(paypalRequest);
        // when
        final PaymentInformation information = service.payment(request);
        // then
        assertAll(
                // payment was not updated so expected payment status is null
                () -> assertEquals(expected(null), information.getPaymentStatus()),
                // payment url also does not exist
                () -> assertEquals(expected(null), information.getPaymentUrl())
        );
    }

    @Test
    void shouldUpdatePayment() {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaypalUpdateResponse paypalUpdateResponse = new PaypalUpdateResponse("approved");

        doReturn(paypalUpdateResponse)
                .when(paypalServicePort)
                .update(updateRequest);

        doNothing()
                .when(paypalRepository)
                .updatePayment(updateRequest);

        // when
        final PaymentUpdateResponse response = service.update(updateRequest);

        // then
        assertEquals(expected("approved"), response.getStatus());
    }

    @Test
    void shouldNotUpdatePaymentWhenPaymentWasNotFound() {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaypalUpdateResponse paypalUpdateResponse = new PaypalUpdateResponse("approved");

        doReturn(paypalUpdateResponse)
                .when(paypalServicePort)
                .update(updateRequest);

        doThrow(new PaymentNotFoundException(404, "Payment not found"))
                .when(paypalRepository)
                .updatePayment(updateRequest);

        // when
        final Executable executable = () -> service.update(updateRequest);
        // then
        final PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class, executable);
        assertAll(
                () -> assertEquals(expected(exception.getCode()), 404),
                () -> assertEquals(expected(exception.getMessage()), "Payment not found")
        );
    }

    @Test
    void shouldNotUpdatePaymentWhenPaymentStatusIsDifferentThanApproved() {
        // given
        final PaymentUpdateRequest updateRequest = PaymentUpdateRequest.builder()
                .paymentId("paymentId")
                .payerId("payerId")
                .build();

        final PaypalUpdateResponse paypalUpdateResponse = new PaypalUpdateResponse("not_approved");

        doReturn(paypalUpdateResponse)
                .when(paypalServicePort)
                .update(updateRequest);

        // when
        final PaymentUpdateResponse response = service.update(updateRequest);
        // then
        assertEquals(expected("NOT_OK"), response.getStatus());
    }

    private <T> T expected(T t) {
        return t;
    }

    private List<Links> createLinks() {
        return List.of(Links.builder()
                .href("href")
                .rel("approval_url")
                .build());
    }

    private Payer createPayer() {
        return Payer.builder()
                .paymentMethod(PaymentMethod.PAYPAL)
                .payerInfo(createPayerInfo())
                .build();
    }

    private PayerInfo createPayerInfo() {
        return PayerInfo.builder()
                .firstName("TEST")
                .lastName("TEST")
                .build();
    }

    private PaypalRequest buildPaypalRequest(PaymentRequest paymentRequest) {
        final Details details = Details.builder()
                .subtotal(paymentRequest.getPrice().toString())
                .build();

        return PaypalRequest.builder()
                .parcelId(paymentRequest.getParcelId())
                .intent(paymentRequest.getIntent())
                .transaction(createTransaction(paymentRequest.getParcelId(),
                        new Amount(paymentRequest.getPrice().toString(), details)))
                .payer(paymentRequest.getPayer())
                .redirectUrls(redirectUrls)
                .build();
    }

    private List<Transaction> createTransaction(Long parcelId, Amount amount) {
        final Transaction transaction = new Transaction();
        transaction.setDescription("Payment for shipment: " + parcelId);
        transaction.setAmount(amount);
        return List.of(transaction);
    }
}
