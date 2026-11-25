package com.warehouse.billing.payment.infrastructure.adapter.primary;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.application.port.primary.command.OpenPaymentCommand;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentIntegrationEventMapper;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PaymentShipmentIntegrationListenerTest {

    private PaymentPort paymentPort;

    private PaymentShipmentIntegrationListener listener;

    @BeforeEach
    void setUp() {
        this.paymentPort = mock(PaymentPort.class);
        this.listener = new PaymentShipmentIntegrationListener(this.paymentPort, new PaymentIntegrationEventMapper());
    }

    @Test
    void shouldOpenPaymentForUnpaidShipmentQuote() {
        this.listener.handle(event(PaymentStateDto.UNPAID, new PriceQuoteId(20L)));

        final OpenPaymentCommand command = captureCommand();
        assertThat(command.priceQuoteId()).isEqualTo(new PriceQuoteId(20L));
        assertThat(command.paymentRequired()).isTrue();
    }

    @Test
    void shouldMarkPaymentNotRequiredForShipmentWithoutPayment() {
        this.listener.handle(event(PaymentStateDto.NOT_REQUIRED, null));

        assertThat(captureCommand().paymentRequired()).isFalse();
    }

    @Test
    void shouldMarkPaymentNotRequiredWhenQuoteIsMissing() {
        this.listener.handle(event(PaymentStateDto.UNPAID, null));

        assertThat(captureCommand().paymentRequired()).isFalse();
    }

    @Test
    void shouldHandleCanceledShipmentWithQuote() {
        this.listener.handle(canceled(new PriceQuoteId(20L)));

        verify(this.paymentPort).handleShipmentCanceled(new PriceQuoteId(20L), ShipmentStatus.CREATED);
    }

    @Test
    void shouldIgnoreCanceledShipmentWithoutQuote() {
        this.listener.handle(canceled(null));

        verifyNoInteractions(this.paymentPort);
    }

    @Test
    void shouldNotPoisonConsumerWhenPaymentCannotBeChanged() {
        doThrow(new PaymentException(PaymentException.Reason.ILLEGAL_STATE, "Payment cannot be refunded"))
                .when(this.paymentPort).handleShipmentCanceled(new PriceQuoteId(20L), ShipmentStatus.CREATED);

        assertThatCode(() -> this.listener.handle(canceled(new PriceQuoteId(20L)))).doesNotThrowAnyException();
    }

    @Test
    void shouldRetryCanceledShipmentWhenGatewayFails() {
        doThrow(new PaymentException(PaymentException.Reason.GATEWAY_FAILURE, "Payment gateway is unavailable"))
                .when(this.paymentPort).handleShipmentCanceled(new PriceQuoteId(20L), ShipmentStatus.CREATED);

        assertThatThrownBy(() -> this.listener.handle(canceled(new PriceQuoteId(20L))))
                .isInstanceOf(PaymentException.class);
    }

    private ShipmentCanceledIntegrationEvent canceled(final PriceQuoteId priceQuoteId) {
        final LocalDateTime createdAt = LocalDateTime.of(2026, 11, 24, 9, 0);
        return new ShipmentCanceledIntegrationEvent(new ShipmentEventData(new ShipmentId(10L), null, null, null,
                null, null, null, createdAt, createdAt, false, false, null, new TrackingNumber("TRK-10"), null, null,
                null, "INTERNAL", PaymentStateDto.PAID, priceQuoteId, null), ShipmentStatus.CREATED);
    }

    private OpenPaymentCommand captureCommand() {
        final ArgumentCaptor<OpenPaymentCommand> command = ArgumentCaptor.forClass(OpenPaymentCommand.class);
        verify(this.paymentPort).open(command.capture());
        return command.getValue();
    }

    private ShipmentCreatedIntegrationEvent event(final PaymentStateDto paymentState,
                                                  final PriceQuoteId priceQuoteId) {
        final LocalDateTime createdAt = LocalDateTime.of(2026, 11, 24, 9, 0);
        return new ShipmentCreatedIntegrationEvent(new ShipmentEventData(new ShipmentId(10L), null, null, null, null,
                null, null, createdAt, createdAt, false, false, null, new TrackingNumber("TRK-10"), null, null, null,
                "INTERNAL", paymentState, priceQuoteId, null), null);
    }
}
