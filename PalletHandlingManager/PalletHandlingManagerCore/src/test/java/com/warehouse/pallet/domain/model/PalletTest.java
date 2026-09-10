package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.enumeration.Unit;
import com.warehouse.pallet.domain.exception.MaxPalletWeightAlreadySetException;
import com.warehouse.pallet.domain.exception.PalletLockedException;
import com.warehouse.pallet.domain.exception.PalletModificationException;
import com.warehouse.pallet.domain.exception.PalletStatusTransitionException;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.SealNumber;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PalletTest {

    private static final PalletId PALLET_ID = new PalletId("pallet-1");
    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("PAL000000000001");
    private static final OperatorId OPERATOR_ID = new OperatorId(1L);

    @Test
    void shouldCreateEmptyPalletWithTrackingNumber() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThat(pallet.getTrackingNumber()).isEqualTo(TRACKING_NUMBER);
        assertThat(pallet.getOperatorId()).isEqualTo(OPERATOR_ID);
        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.CREATED);
        assertThat(pallet.isEmpty()).isTrue();
        assertThat(pallet.loadBlockReason()).contains(PalletLoadBlockReason.EMPTY);
    }

    @Test
    void shouldNormalizeTrackingNumber() {
        assertThat(new TrackingNumber(" pal123 ").value()).isEqualTo("PAL123");
    }

    @Test
    void shouldRejectBlankTrackingNumber() {
        assertThatThrownBy(() -> new TrackingNumber(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldCreateReadyPalletWithShipments() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L), new ShipmentId(2L)));

        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.READY);
        assertThat(pallet.getShipmentIds()).hasSize(2);
        assertThat(pallet.canBeLoaded()).isTrue();
    }

    @Test
    void shouldRejectPalletWithoutShipments() {
        assertThatThrownBy(() -> Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID, List.of()))
                .isInstanceOf(PalletModificationException.class);
    }

    @Test
    void shouldGoBackToPreparingWhenLastShipmentRemoved() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);
        pallet.addShipment(new ShipmentId(1L));

        pallet.removeShipment(new ShipmentId(1L));

        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.PREPARING);
        assertThat(pallet.isEmpty()).isTrue();
    }

    @Test
    void shouldMarkPalletNotReadyWhenOverweight() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);
        pallet.addShipment(new ShipmentId(1L));
        pallet.changeMaxPalletWeight(new BigDecimal("1000"));

        pallet.changeWeight(new Weight(new BigDecimal("1.2"), Unit.T));

        assertThat(pallet.isOverweight()).isTrue();
        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.NOT_READY);
        assertThat(pallet.loadBlockReason()).contains(PalletLoadBlockReason.OVERWEIGHT);
        assertThatThrownBy(pallet::markLoaded).isInstanceOf(PalletModificationException.class);
    }

    @Test
    void shouldBecomeReadyAgainWhenWeightWithinLimit() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);
        pallet.addShipment(new ShipmentId(1L));
        pallet.changeMaxPalletWeight(new BigDecimal("1000"));
        pallet.changeWeight(new Weight(new BigDecimal("1200"), Unit.KG));

        pallet.changeWeight(new Weight(new BigDecimal("900000"), Unit.G));

        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.READY);
    }

    @Test
    void shouldNotAllowChangingMaxWeightOnceSet() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);
        pallet.changeMaxPalletWeight(new BigDecimal("1000"));

        assertThatThrownBy(() -> pallet.changeMaxPalletWeight(new BigDecimal("1200")))
                .isInstanceOf(MaxPalletWeightAlreadySetException.class);
        assertThat(pallet.getMaxPalletWeight().getValue()).isEqualByComparingTo("1000");
    }

    @Test
    void shouldRejectNonPositiveMaxWeight() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThatThrownBy(() -> pallet.changeMaxPalletWeight(BigDecimal.ZERO))
                .isInstanceOf(PalletModificationException.class);
    }

    @Test
    void shouldLoadReadyPalletOnlyOnce() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));

        assertThat(pallet.markLoaded()).isTrue();
        assertThat(pallet.markLoaded()).isFalse();
        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.LOADED);
    }

    @Test
    void shouldNotLoadLockedPallet() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        pallet.changeStorageStatus(StorageStatus.LOCKED);

        assertThat(pallet.loadBlockReason()).contains(PalletLoadBlockReason.LOCKED);
        assertThatThrownBy(pallet::markLoaded).isInstanceOf(PalletModificationException.class);
        assertThatThrownBy(() -> pallet.addShipment(new ShipmentId(2L))).isInstanceOf(PalletLockedException.class);
    }

    @Test
    void shouldNotChangeContentOfLoadedPallet() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        pallet.markLoaded();

        assertThatThrownBy(() -> pallet.addShipment(new ShipmentId(2L)))
                .isInstanceOf(PalletModificationException.class);
    }

    @Test
    void shouldFollowFullLifecycle() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));

        pallet.markLoaded();
        pallet.changePalletStatus(PalletStatus.IN_TRANSIT);
        pallet.changePalletStatus(PalletStatus.DELIVERED);

        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.DELIVERED);
    }

    @Test
    void shouldChangeTransportStatusManually() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        pallet.markLoaded();

        assertThat(pallet.changeTransportStatus(PalletStatus.IN_TRANSIT)).isEqualTo(PalletStatus.LOADED);
        assertThat(pallet.changeTransportStatus(PalletStatus.DELIVERED)).isEqualTo(PalletStatus.IN_TRANSIT);
    }

    @Test
    void shouldNotSetCalculatedStatusesManually() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));

        assertThatThrownBy(() -> pallet.changeTransportStatus(PalletStatus.LOADED))
                .isInstanceOf(PalletStatusTransitionException.class);
        assertThatThrownBy(() -> pallet.changeTransportStatus(PalletStatus.DELIVERED))
                .isInstanceOf(PalletStatusTransitionException.class);
    }

    @Test
    void shouldUpdatePalletAttributes() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        pallet.update(PalletType.CHILD, PalletHandlingPriority.HIGH, true, new Dimension(120, 80, 150),
                new SealNumber(" S-1 "));

        assertThat(pallet.getPalletType()).isEqualTo(PalletType.CHILD);
        assertThat(pallet.getPalletHandlingPriority()).isEqualTo(PalletHandlingPriority.HIGH);
        assertThat(pallet.isRefrigerated()).isTrue();
        assertThat(pallet.getDimension().getLength()).isEqualTo(120);
        assertThat(pallet.getSealNumber().value()).isEqualTo("S-1");
    }

    @Test
    void shouldRejectInvalidPalletUpdate() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThatThrownBy(() -> pallet.update(null, null, null, new Dimension(0, 80, 150), null))
                .isInstanceOf(PalletModificationException.class);
        pallet.cancel();
        assertThatThrownBy(() -> pallet.update(PalletType.CHILD, null, null, null, null))
                .isInstanceOf(PalletModificationException.class);
    }

    @Test
    void shouldRejectInvalidTransitions() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThatThrownBy(() -> pallet.changePalletStatus(PalletStatus.LOADED))
                .isInstanceOf(PalletStatusTransitionException.class);
        assertThatThrownBy(() -> pallet.changePalletStatus(PalletStatus.DELIVERED))
                .isInstanceOf(PalletStatusTransitionException.class);
    }

    @Test
    void shouldCancelPalletIdempotently() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThat(pallet.cancel()).isTrue();
        assertThat(pallet.cancel()).isFalse();
        assertThatThrownBy(() -> pallet.changePalletStatus(PalletStatus.READY))
                .isInstanceOf(PalletStatusTransitionException.class);
    }

    @Test
    void shouldNotCancelLoadedPallet() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        pallet.markLoaded();

        assertThatThrownBy(pallet::cancel).isInstanceOf(PalletStatusTransitionException.class);
    }

    @Test
    void shouldCheckOwnership() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, OPERATOR_ID);

        assertThat(pallet.belongsTo(OPERATOR_ID)).isTrue();
        assertThat(pallet.belongsTo(new OperatorId(2L))).isFalse();
    }
}
