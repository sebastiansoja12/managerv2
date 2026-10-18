package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PalletScanPortImplTest {

    private static final PalletScanCommand COMMAND = new PalletScanCommand(PalletFixture.TRACKING_NUMBER);

    private PalletHandlingManagerServicePort palletHandlingManagerServicePort;
    private ShipmentServicePort shipmentServicePort;
    private DomainEventPublisher domainEventPublisher;
    private PalletScanPortImpl palletScanPort;

    @BeforeEach
    void setUp() {
        this.palletHandlingManagerServicePort = mock(PalletHandlingManagerServicePort.class);
        this.shipmentServicePort = mock(ShipmentServicePort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.palletScanPort = new PalletScanPortImpl(this.palletHandlingManagerServicePort, this.shipmentServicePort,
                this.domainEventPublisher);
    }

    @Test
    void shouldLoadAllShipmentsAndPublishPalletLoaded() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenReturn(PalletFixture.readyPallet(1L, 2L));
        when(this.shipmentServicePort.loadOnPallet(any())).thenAnswer(invocation ->
                ShipmentLoadResult.loaded(invocation.getArgument(0), "SENT"));

        final PalletScanResult result = this.palletScanPort.scan(COMMAND);

        final ArgumentCaptor<PalletLoaded> event = ArgumentCaptor.forClass(PalletLoaded.class);
        verify(this.domainEventPublisher).publish(event.capture());
        assertThat(result.success()).isTrue();
        assertThat(result.shipments()).hasSize(2).allMatch(ShipmentLoadResult::loaded);
        assertThat(event.getValue().getSnapshot().palletId()).isEqualTo(PalletFixture.PALLET_ID);
        assertThat(event.getValue().getSnapshot().shipmentIds())
                .containsExactly(new ShipmentId(1L), new ShipmentId(2L));
    }

    @Test
    void shouldNotPublishEventWhenSomeShipmentsFailed() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenReturn(PalletFixture.readyPallet(1L, 2L));
        when(this.shipmentServicePort.loadOnPallet(new ShipmentId(1L)))
                .thenReturn(ShipmentLoadResult.loaded(new ShipmentId(1L), "SENT"));
        when(this.shipmentServicePort.loadOnPallet(new ShipmentId(2L)))
                .thenReturn(ShipmentLoadResult.failed(new ShipmentId(2L), "Delivered shipment cannot be changed"));

        final PalletScanResult result = this.palletScanPort.scan(COMMAND);

        assertThat(result.success()).isFalse();
        assertThat(result.errorCode()).isEqualTo(PalletScanErrorCode.SHIPMENT_STATUS_UPDATE_FAILED);
        assertThat(result.shipments()).hasSize(2);
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldRejectEmptyPalletWithoutTouchingShipments() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenReturn(PalletFixture.blockedPallet(PalletStatus.CREATED, PalletLoadBlockReason.EMPTY));

        final PalletScanResult result = this.palletScanPort.scan(COMMAND);

        assertThat(result.success()).isFalse();
        assertThat(result.errorCode()).isEqualTo(PalletScanErrorCode.PALLET_EMPTY);
        verifyNoInteractions(this.shipmentServicePort, this.domainEventPublisher);
    }

    @Test
    void shouldRejectLockedPallet() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenReturn(PalletFixture.blockedPallet(PalletStatus.READY, PalletLoadBlockReason.LOCKED, 1L));

        final PalletScanResult result = this.palletScanPort.scan(COMMAND);

        assertThat(result.errorCode()).isEqualTo(PalletScanErrorCode.PALLET_LOCKED);
        verifyNoInteractions(this.shipmentServicePort, this.domainEventPublisher);
    }

    @Test
    void shouldHandleRescanOfLoadedPalletWithoutNewEvent() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenReturn(PalletFixture.loadedPallet(1L));
        when(this.shipmentServicePort.loadOnPallet(new ShipmentId(1L)))
                .thenReturn(ShipmentLoadResult.loaded(new ShipmentId(1L), "SENT"));

        final PalletScanResult result = this.palletScanPort.scan(COMMAND);

        assertThat(result.success()).isTrue();
        assertThat(result.alreadyLoaded()).isTrue();
        verify(this.domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldPropagatePalletNotFound() {
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(PalletFixture.TRACKING_NUMBER))
                .thenThrow(new PalletRequestRejectedException(404, "PALLET_NOT_FOUND", "Pallet not found"));

        assertThatThrownBy(() -> this.palletScanPort.scan(COMMAND))
                .isInstanceOf(PalletRequestRejectedException.class);
        verifyNoInteractions(this.shipmentServicePort, this.domainEventPublisher);
    }
}
