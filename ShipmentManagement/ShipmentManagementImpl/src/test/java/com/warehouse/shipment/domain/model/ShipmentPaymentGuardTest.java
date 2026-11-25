package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.enumeration.CancellationReason;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import com.warehouse.shipment.application.service.status.ShipmentAcceptedStatusChangeStrategy;
import com.warehouse.shipment.application.service.status.ShipmentDeliveredStatusChangeStrategy;
import com.warehouse.shipment.application.service.status.ShipmentPreparedStatusChangeStrategy;
import com.warehouse.shipment.application.service.status.ShipmentRedirectedStatusChangeStrategy;
import com.warehouse.shipment.application.service.status.ShipmentSentStatusChangeStrategy;
import com.warehouse.shipment.domain.enumeration.PaymentState;
import com.warehouse.shipment.domain.exception.ShipmentModificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShipmentPaymentGuardTest {

    private static final String UNPAID_MESSAGE = "Shipment must be paid before it leaves draft statuses";

    static Stream<Arguments> nonDraftTransitions() {
        return Stream.of(
                Arguments.of("notifyShipmentCollected", (Consumer<Shipment>) Shipment::notifyShipmentCollected),
                Arguments.of("acceptInDepartment", (Consumer<Shipment>) Shipment::acceptInDepartment),
                Arguments.of("notifyShipmentLoadedOnPallet",
                        (Consumer<Shipment>) Shipment::notifyShipmentLoadedOnPallet),
                Arguments.of("handOver", (Consumer<Shipment>) Shipment::handOver),
                Arguments.of("notifyShipmentAccepted", (Consumer<Shipment>) Shipment::notifyShipmentAccepted),
                Arguments.of("notifyShipmentSent", (Consumer<Shipment>) Shipment::notifyShipmentSent),
                Arguments.of("prepareShipmentToSend", (Consumer<Shipment>) Shipment::prepareShipmentToSend),
                Arguments.of("prepareShipmentToDeliver", (Consumer<Shipment>) Shipment::prepareShipmentToDeliver),
                Arguments.of("markAsDelivered", (Consumer<Shipment>) Shipment::markAsDelivered),
                Arguments.of("changeShipmentStatus to SENT",
                        (Consumer<Shipment>) shipment -> shipment.changeShipmentStatus(ShipmentStatus.SENT)),
                Arguments.of("ACCEPTED strategy",
                        (Consumer<Shipment>) shipment -> new ShipmentAcceptedStatusChangeStrategy().process(shipment)),
                Arguments.of("SENT strategy",
                        (Consumer<Shipment>) shipment -> new ShipmentSentStatusChangeStrategy().process(shipment)),
                Arguments.of("DELIVERY strategy",
                        (Consumer<Shipment>) shipment -> new ShipmentDeliveredStatusChangeStrategy().process(shipment)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nonDraftTransitions")
    void shouldRejectEveryNonDraftTransitionWhenUnpaid(final String transitionName,
                                                       final Consumer<Shipment> transition) {
        final Shipment shipment = ShipmentFixture.unpaidCreated();

        assertThatThrownBy(() -> transition.accept(shipment))
                .isInstanceOf(ShipmentModificationException.class)
                .hasMessage(UNPAID_MESSAGE);
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.CREATED);
    }

    @Test
    void shouldRejectHandOverOfUnpaidReroutedShipment() {
        final Shipment shipment = ShipmentFixture.unpaid(ShipmentStatus.REROUTE);

        assertThatThrownBy(shipment::handOver)
                .isInstanceOf(ShipmentModificationException.class)
                .hasMessage(UNPAID_MESSAGE);
    }

    @Test
    void shouldAllowCollectionAfterPayment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();

        shipment.markAsPaid();
        shipment.notifyShipmentCollected();

        assertThat(shipment.getPaymentState()).isEqualTo(PaymentState.PAID);
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.COLLECTED);
    }

    @Test
    void shouldAllowShipmentWithoutRequiredPaymentToLeaveDraft() {
        final Shipment shipment = ShipmentFixture.paymentNotRequired(ShipmentStatus.CREATED);

        shipment.notifyShipmentCollected();

        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.COLLECTED);
    }

    @Test
    void shouldAllowUnpaidShipmentToStayPrepared() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();

        new ShipmentPreparedStatusChangeStrategy().process(shipment);

        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.PREPARED);
    }

    @Test
    void shouldAllowCancellationLossAndRerouteOfUnpaidShipment() {
        final Shipment canceled = ShipmentFixture.unpaidCreated();
        final Shipment lost = ShipmentFixture.unpaidCreated();
        final Shipment rerouted = ShipmentFixture.unpaidCreated();

        canceled.markAsCanceledWithoutPolicy(CancellationReason.CUSTOMER_REQUEST, LocalDateTime.now());
        lost.markAsLost();
        rerouted.notifyShipmentRerouted();

        assertThat(canceled.getShipmentStatus()).isEqualTo(ShipmentStatus.CANCELED);
        assertThat(lost.getShipmentStatus()).isEqualTo(ShipmentStatus.LOST);
        assertThat(rerouted.getShipmentStatus()).isEqualTo(ShipmentStatus.REROUTE);
    }

    @Test
    void shouldIgnoreRepeatedPayment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        shipment.markAsPaid();

        assertThatCode(shipment::markAsPaid).doesNotThrowAnyException();

        assertThat(shipment.getPaymentState()).isEqualTo(PaymentState.PAID);
    }

    @Test
    void shouldRejectPaymentWhenPaymentIsNotRequired() {
        final Shipment shipment = ShipmentFixture.paymentNotRequired(ShipmentStatus.CREATED);

        assertThatThrownBy(shipment::markAsPaid).isInstanceOf(ShipmentModificationException.class);
    }

    @Test
    void shouldCancelExpiredPaymentEvenWhenCancellationWindowIsClosed() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        final LocalDateTime currentTime = LocalDateTime.now().plusDays(3);
        assertThatThrownBy(() -> shipment.cancel(ShipmentFixture.closedCancellationWindow(), currentTime))
                .isInstanceOf(ShipmentModificationException.class);

        shipment.cancelForExpiredPayment(currentTime);

        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.CANCELED);
        assertThat(shipment.getCancellationReason()).isEqualTo(CancellationReason.PAYMENT_EXPIRED);
        assertThat(shipment.getCancelledAt()).isEqualTo(currentTime);
        assertThat(shipment.getLocked()).isTrue();
    }

    @Test
    void shouldIgnoreExpiredPaymentOfCanceledShipment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        final LocalDateTime cancelledAt = LocalDateTime.now();
        shipment.markAsCanceledWithoutPolicy(CancellationReason.CUSTOMER_REQUEST, cancelledAt);

        shipment.cancelForExpiredPayment(cancelledAt.plusDays(1));

        assertThat(shipment.getCancellationReason()).isEqualTo(CancellationReason.CUSTOMER_REQUEST);
        assertThat(shipment.getCancelledAt()).isEqualTo(cancelledAt);
    }

    @Test
    void shouldRejectExpiredPaymentCancellationOfPaidShipment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        shipment.markAsPaid();

        assertThatThrownBy(() -> shipment.cancelForExpiredPayment(LocalDateTime.now()))
                .isInstanceOf(ShipmentModificationException.class);
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.CREATED);
    }

    @Test
    void shouldRejectExpiredPaymentCancellationOutsideDraftStatuses() {
        final Shipment shipment = ShipmentFixture.unpaid(ShipmentStatus.REROUTE);

        assertThatThrownBy(() -> shipment.cancelForExpiredPayment(LocalDateTime.now()))
                .isInstanceOf(ShipmentModificationException.class);
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.REROUTE);
    }

    static Stream<Arguments> derivedShipments() {
        return Stream.of(
                Arguments.of("redirectToSender", (Function<Shipment, Shipment>) shipment ->
                        shipment.redirectToSender(new ShipmentId(2L), new TrackingNumber("MGR-2"),
                                ExternalId.randomUUID(), ShipmentWorkflowSettings.defaults())),
                Arguments.of("returnToSender", (Function<Shipment, Shipment>) shipment ->
                        shipment.returnToSender(new ShipmentId(2L), new TrackingNumber("MGR-2"),
                                ShipmentWorkflowSettings.defaults(), ExternalId.randomUUID())),
                Arguments.of("prepareReturnShipmentDraft", (Function<Shipment, Shipment>) shipment -> {
                    shipment.markAsDelivered();
                    return shipment.prepareReturnShipmentDraft(new ShipmentId(2L), new TrackingNumber("MGR-2"),
                            ShipmentWorkflowSettings.defaults(), ExternalId.randomUUID());
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("derivedShipments")
    void shouldCreateDerivedShipmentAsPaymentNotRequired(final String derivationName,
                                                         final Function<Shipment, Shipment> derivation) {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        shipment.markAsPaid();

        final Shipment derived = derivation.apply(shipment);

        assertThat(derived.getPaymentState()).isEqualTo(PaymentState.NOT_REQUIRED);
        assertThat(derived.getPriceQuoteId()).isNull();
        assertThat(derived.getBillingCustomerId()).isNull();
    }

    @Test
    void shouldRejectSplitOfUnpaidShipment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();

        assertThatThrownBy(() -> shipment.changeShipmentTypeWithRelatedId(ShipmentType.CHILD, new ShipmentId(2L)))
                .isInstanceOf(ShipmentModificationException.class)
                .hasMessage(UNPAID_MESSAGE);
        assertThat(shipment.getShipmentType()).isEqualTo(ShipmentType.PARENT);
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(shipment.getShipmentRelatedId()).isNull();
    }

    @Test
    void shouldAllowSplitOfPaidShipment() {
        final Shipment shipment = ShipmentFixture.unpaidCreated();
        shipment.markAsPaid();

        shipment.changeShipmentTypeWithRelatedId(ShipmentType.CHILD, new ShipmentId(2L));

        assertThat(shipment.getShipmentType()).isEqualTo(ShipmentType.CHILD);
        assertThat(shipment.getShipmentRelatedId()).isEqualTo(new ShipmentId(2L));
    }

    static Stream<Arguments> redirects() {
        return Stream.of(
                Arguments.of("notifyRelatedShipmentRedirected", (Consumer<Shipment>) shipment ->
                        shipment.notifyRelatedShipmentRedirected(new ShipmentId(2L))),
                Arguments.of("prepareShipmentToRedirect", (Consumer<Shipment>) shipment ->
                        shipment.prepareShipmentToRedirect(new ShipmentId(2L))),
                Arguments.of("REDIRECT strategy", (Consumer<Shipment>) shipment ->
                        new ShipmentRedirectedStatusChangeStrategy().process(shipment)),
                Arguments.of("redirectToSender", (Consumer<Shipment>) shipment ->
                        shipment.redirectToSender(new ShipmentId(2L), new TrackingNumber("MGR-2"),
                                ExternalId.randomUUID(), ShipmentWorkflowSettings.defaults())));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("redirects")
    void shouldRejectRedirectOfUnpaidShipment(final String redirectName, final Consumer<Shipment> redirect) {
        final Shipment shipment = ShipmentFixture.unpaidCreated();

        assertThatThrownBy(() -> redirect.accept(shipment))
                .isInstanceOf(ShipmentModificationException.class)
                .hasMessage(UNPAID_MESSAGE);
        assertThat(shipment.getShipmentId()).isEqualTo(new ShipmentId(1L));
        assertThat(shipment.getShipmentStatus()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(shipment.getPaymentState()).isEqualTo(PaymentState.UNPAID);
        assertThat(shipment.getShipmentRelatedId()).isNull();
    }
}
