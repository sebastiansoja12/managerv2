package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.application.port.primary.command.RecordCounterPaymentCommand;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.model.PaymentFixture;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.PaymentEntity;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private PaymentPort paymentPort;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.paymentPort = mock(PaymentPort.class);
        final FormattingConversionService conversion = new FormattingConversionService();
        conversion.addConverter(String.class, PriceQuoteId.class, value -> new PriceQuoteId(Long.valueOf(value)));
        conversion.addConverter(String.class, PaymentId.class, value -> new PaymentId(Long.valueOf(value)));
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new PaymentController(this.paymentPort, new PaymentWebMapper()))
                .setControllerAdvice(new PaymentExceptionHandler())
                .setConversionService(conversion)
                .build();
    }

    @Test
    void shouldRecordCounterPayment() throws Exception {
        when(this.paymentPort.recordCounterPayment(any())).thenReturn(PaymentFixture.pending());

        this.mockMvc.perform(post("/billing/quotes/20/counter-payment").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"method":"CASH","buyerTaxId":"5260250995"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId.value").value(1));

        final ArgumentCaptor<RecordCounterPaymentCommand> command =
                ArgumentCaptor.forClass(RecordCounterPaymentCommand.class);
        verify(this.paymentPort).recordCounterPayment(command.capture());
        assertThat(command.getValue().priceQuoteId()).isEqualTo(new PriceQuoteId(20L));
        assertThat(command.getValue().method()).isEqualTo(PaymentMethod.CASH);
        assertThat(command.getValue().buyerTaxId().value()).isEqualTo("5260250995");
    }

    @Test
    void shouldReturn400WhenCounterPaymentUsesStripe() throws Exception {
        when(this.paymentPort.recordCounterPayment(any())).thenThrow(new PaymentException(
                PaymentException.Reason.INVALID_REQUEST, "Counter payment accepts only cash or card"));

        this.mockMvc.perform(post("/billing/quotes/20/counter-payment").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"method":"STRIPE"}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenCounterPaymentLosesOptimisticLock() throws Exception {
        when(this.paymentPort.recordCounterPayment(any())).thenThrow(
                new ObjectOptimisticLockingFailureException(PaymentEntity.class, 1L));

        this.mockMvc.perform(post("/billing/quotes/20/counter-payment").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"method":"CASH"}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn409WhenPersistenceReportsOptimisticLock() throws Exception {
        when(this.paymentPort.recordCounterPayment(any())).thenThrow(new OptimisticLockException("stale payment"));

        this.mockMvc.perform(post("/billing/quotes/20/counter-payment").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"method":"CASH"}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRequestManualRefund() throws Exception {
        when(this.paymentPort.requestManualRefund(new PaymentId(1L)))
                .thenReturn(PaymentFixture.withStatus(PaymentStatus.REFUND_PENDING));

        this.mockMvc.perform(post("/billing/payments/1/refund"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REFUND_PENDING"));
    }

    @Test
    void shouldReturn409WhenRefundedPaymentIsNotPaid() throws Exception {
        when(this.paymentPort.requestManualRefund(any())).thenThrow(new PaymentException(
                PaymentException.Reason.ILLEGAL_STATE, "Payment cannot be refunded in status PENDING"));

        this.mockMvc.perform(post("/billing/payments/1/refund"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn502WhenGatewayRejectsRefund() throws Exception {
        when(this.paymentPort.requestManualRefund(any())).thenThrow(new PaymentException(
                PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable"));

        this.mockMvc.perform(post("/billing/payments/1/refund"))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldConfirmCounterRefund() throws Exception {
        final Payment payment = PaymentFixture.pending();
        payment.markPaidAtCounter(PaymentMethod.CASH, PaymentFixture.USER_ID, PaymentFixture.DEPARTMENT_ID, null,
                PaymentFixture.NOW);
        payment.requestRefund(PaymentFixture.NOW, () -> null);
        payment.markRefundedAtCounter(new UserId(9L), PaymentFixture.NOW.plusSeconds(60));
        when(this.paymentPort.confirmCounterRefund(new PaymentId(1L))).thenReturn(payment);

        this.mockMvc.perform(post("/billing/payments/1/counter-refund"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REFUNDED"))
                .andExpect(jsonPath("$.refundRequestedAt").isNotEmpty())
                .andExpect(jsonPath("$.refundedAt").isNotEmpty())
                .andExpect(jsonPath("$.refundedBy.value").value(9));
    }

    @Test
    void shouldReturn400ForUnboundedPageSize() throws Exception {
        this.mockMvc.perform(get("/billing/payments").param("size", "101"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/billing/payments").param("page", "-1"))
                .andExpect(status().isBadRequest());
    }
}
