package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.StripeWebhookMapper;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.WebhookEventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.warehouse.billing.payment.infrastructure.adapter.primary.StripeWebhookFixture.WEBHOOK_SECRET;
import static com.warehouse.billing.payment.infrastructure.adapter.primary.StripeWebhookFixture.chargeRefundedEvent;
import static com.warehouse.billing.payment.infrastructure.adapter.primary.StripeWebhookFixture.checkoutEvent;
import static com.warehouse.billing.payment.infrastructure.adapter.primary.StripeWebhookFixture.signature;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StripeWebhookControllerTest {

    private PaymentPort paymentPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.paymentPort = mock(PaymentPort.class);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new StripeWebhookController(this.paymentPort,
                        new StripeWebhookEventVerifier(WEBHOOK_SECRET), new StripeWebhookMapper()))
                .setControllerAdvice(new StripeWebhookExceptionHandler())
                .build();
    }

    @Test
    void shouldConfirmPaymentForSignedCompletedCheckout() throws Exception {
        final String payload = checkoutEvent("evt_1", "checkout.session.completed", "cs_test_1", "paid");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isOk());

        verify(this.paymentPort).confirmOnlinePayment(
                new ConfirmOnlinePaymentCommand(new PaymentId(1L), "cs_test_1", "pi_test_1",
                        new WebhookEventId("evt_1")));
    }

    @Test
    void shouldConfirmPaymentForSucceededAsyncCheckout() throws Exception {
        final String payload = checkoutEvent("evt_2", "checkout.session.async_payment_succeeded", "cs_test_1",
                "paid");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isOk());

        verify(this.paymentPort).confirmOnlinePayment(
                new ConfirmOnlinePaymentCommand(new PaymentId(1L), "cs_test_1", "pi_test_1",
                        new WebhookEventId("evt_2")));
    }

    @Test
    void shouldNotConfirmCompletedCheckoutWithUnpaidStatus() throws Exception {
        final String payload = checkoutEvent("evt_3", "checkout.session.completed", "cs_test_1", "unpaid");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isOk());

        verifyNoInteractions(this.paymentPort);
    }

    @Test
    void shouldRejectWebhookWithInvalidSignature() throws Exception {
        final String payload = checkoutEvent("evt_1", "checkout.session.completed", "cs_test_1", "paid");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, "whsec_other_secret"))
                        .content(payload))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(this.paymentPort);
    }

    @Test
    void shouldRejectWebhookWithoutSignature() throws Exception {
        final String payload = checkoutEvent("evt_1", "checkout.session.completed", "cs_test_1", "paid");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(this.paymentPort);
    }

    @Test
    void shouldAcknowledgeCompletionOfPaymentThatCannotBePaid() throws Exception {
        final String payload = checkoutEvent("evt_4", "checkout.session.completed", "cs_test_1", "paid");
        when(this.paymentPort.confirmOnlinePayment(any())).thenThrow(new PaymentException(
                PaymentException.Reason.ILLEGAL_STATE, "Payment cannot be marked as paid in status EXPIRED"));

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void shouldConfirmRefundForRefundedCharge() throws Exception {
        final String payload = chargeRefundedEvent("evt_5", "pi_test_1");

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isOk());

        verify(this.paymentPort).confirmGatewayRefund(
                new ConfirmGatewayRefundCommand("pi_test_1", new WebhookEventId("evt_5")));
        verify(this.paymentPort, never()).confirmOnlinePayment(any());
    }

    @Test
    void shouldAskStripeToRetryWhenRefundIsNotRecordedYet() throws Exception {
        final String payload = chargeRefundedEvent("evt_6", "pi_unknown");
        doThrow(new PaymentNotReadyException("Refund of the charge is not recorded yet"))
                .when(this.paymentPort).confirmGatewayRefund(any());

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void shouldAskStripeToRetryWhenGatewayFails() throws Exception {
        final String payload = checkoutEvent("evt_7", "checkout.session.completed", "cs_test_1", "paid");
        when(this.paymentPort.confirmOnlinePayment(any())).thenThrow(new PaymentException(
                PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable"));

        this.mockMvc.perform(post("/billing/webhooks/stripe").contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature(payload, WEBHOOK_SECRET))
                        .content(payload))
                .andExpect(status().isServiceUnavailable());
    }
}
