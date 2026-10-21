package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.exception.PalletModificationException;
import com.warehouse.pallet.domain.exception.PalletNotFoundException;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletUpdate;
import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.model.PalletLoadedRequest;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.service.SealNumberService;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.warehouse.pallet.domain.enumeration.PalletHistoryEventType;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;
import com.warehouse.pallet.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.pallet.domain.port.secondary.PalletHistoryRepository;
import com.warehouse.pallet.domain.vo.UserContext;
import com.warehouse.pallet.domain.vo.UserId;
import com.warehouse.pallet.domain.enumeration.PalletStatus;

import java.util.Optional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PalletPortImplTest {

    private static final PalletId PALLET_ID = new PalletId("p1");
    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("PAL1");

    private static final UserContext USER_CONTEXT = new UserContext(new UserId(3L, "warehouse.user"),
            new OperatorId(1L), new DepartmentId(10L));

    private PalletStorageService palletStorageService;
    private PalletHistoryRepository palletHistoryRepository;
    private PalletPortImpl palletPort;

    @BeforeEach
    void setUp() {
        this.palletStorageService = mock(PalletStorageService.class);
        this.palletHistoryRepository = mock(PalletHistoryRepository.class);
        final CurrentUserServicePort currentUserServicePort = mock(CurrentUserServicePort.class);
        when(currentUserServicePort.currentUser()).thenReturn(Optional.of(USER_CONTEXT));
        this.palletPort = new PalletPortImpl(this.palletStorageService, mock(DriverStorageService.class),
                mock(SealNumberService.class), this.palletHistoryRepository, currentUserServicePort);
    }

    @Test
    void shouldCreateEmptyPalletWithGeneratedTrackingNumber() {
        final OperatorId operatorId = new OperatorId(1L);
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, operatorId);
        when(this.palletStorageService.nextPalletId()).thenReturn(PALLET_ID);
        when(this.palletStorageService.nextTrackingNumber()).thenReturn(TRACKING_NUMBER);
        when(this.palletStorageService.createEmptyPallet(PALLET_ID, TRACKING_NUMBER, operatorId, new DepartmentId(10L)))
                .thenReturn(pallet);

        assertThat(this.palletPort.createEmptyPallet(operatorId, new DepartmentId(10L)).getTrackingNumber()).isEqualTo(TRACKING_NUMBER);
    }

    @Test
    void shouldHidePalletOfAnotherOperator() {
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, new OperatorId(1L));
        when(this.palletStorageService.findByTrackingNumber(TRACKING_NUMBER)).thenReturn(pallet);

        assertThatThrownBy(() -> this.palletPort.getPalletByTrackingNumber(TRACKING_NUMBER, new OperatorId(2L)))
                .isInstanceOf(PalletNotFoundException.class);
        assertThat(this.palletPort.getPalletByTrackingNumber(TRACKING_NUMBER, new OperatorId(1L))).isSameAs(pallet);
    }

    @Test
    void shouldApplyPalletLoadedEvent() {
        this.palletPort.handlePalletLoaded(new PalletLoadedRequest("event-1", PALLET_ID));

        verify(this.palletStorageService).markLoaded(PALLET_ID);
    }

    @Test
    void shouldIgnorePalletLoadedEventThatCannotBeApplied() {
        when(this.palletStorageService.markLoaded(PALLET_ID))
                .thenThrow(new PalletModificationException("Pallet cannot be loaded: EMPTY"));

        assertThatCode(() -> this.palletPort.handlePalletLoaded(new PalletLoadedRequest("event-1", PALLET_ID)))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldCancelOwnedPallet() {
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, new OperatorId(1L),
                List.of(new ShipmentId(1L)));
        when(this.palletStorageService.find(PALLET_ID)).thenReturn(pallet);
        when(this.palletStorageService.cancel(PALLET_ID)).thenReturn(pallet);

        this.palletPort.cancelPallet(PALLET_ID, new OperatorId(1L));

        verify(this.palletStorageService).cancel(PALLET_ID);
    }

    @Test
    void shouldRecordHistoryWithUserFromJwt() {
        final OperatorId operatorId = new OperatorId(1L);
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, operatorId);
        when(this.palletStorageService.nextPalletId()).thenReturn(PALLET_ID);
        when(this.palletStorageService.nextTrackingNumber()).thenReturn(TRACKING_NUMBER);
        when(this.palletStorageService.createEmptyPallet(PALLET_ID, TRACKING_NUMBER, operatorId, new DepartmentId(10L)))
                .thenReturn(pallet);

        this.palletPort.createEmptyPallet(operatorId, new DepartmentId(10L));

        final ArgumentCaptor<PalletHistoryEntry> entry = ArgumentCaptor.forClass(PalletHistoryEntry.class);
        verify(this.palletHistoryRepository).append(entry.capture());
        assertThat(entry.getValue().eventType()).isEqualTo(PalletHistoryEventType.CREATED);
        assertThat(entry.getValue().userId().username()).isEqualTo("warehouse.user");
        assertThat(entry.getValue().departmentId()).isEqualTo(new DepartmentId(10L));
    }

    @Test
    void shouldNotRecordRepeatedLoadedEvent() {
        when(this.palletStorageService.markLoaded(PALLET_ID)).thenReturn(false);

        this.palletPort.handlePalletLoaded(new PalletLoadedRequest("event-2", PALLET_ID));

        verify(this.palletHistoryRepository, never()).append(any());
    }

    @Test
    void shouldRecordPalletUpdate() {
        final OperatorId operatorId = new OperatorId(1L);
        final Pallet pallet = Pallet.empty(PALLET_ID, TRACKING_NUMBER, operatorId);
        when(this.palletStorageService.find(PALLET_ID)).thenReturn(pallet);
        when(this.palletStorageService.update(any(), any(), any(), any(), any(), any())).thenAnswer(invocation -> {
            pallet.update(invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3),
                    invocation.getArgument(4), invocation.getArgument(5));
            return pallet;
        });

        this.palletPort.updatePallet(PALLET_ID, new PalletUpdate(PalletType.CHILD, PalletHandlingPriority.HIGH, false,
                null, new SealNumber("S-9")), operatorId);

        final ArgumentCaptor<PalletHistoryEntry> entry = ArgumentCaptor.forClass(PalletHistoryEntry.class);
        verify(this.palletHistoryRepository).append(entry.capture());
        assertThat(entry.getValue().eventType()).isEqualTo(PalletHistoryEventType.PALLET_UPDATED);
        assertThat(entry.getValue().details()).contains("CHILD", "HIGH", "S-9");
    }

    @Test
    void shouldRecordManualStatusChange() {
        final OperatorId operatorId = new OperatorId(1L);
        final Pallet pallet = Pallet.withShipments(PALLET_ID, TRACKING_NUMBER, operatorId, List.of(new ShipmentId(1L)));
        when(this.palletStorageService.find(PALLET_ID)).thenReturn(pallet);
        when(this.palletStorageService.changeTransportStatus(PALLET_ID, PalletStatus.IN_TRANSIT))
                .thenReturn(PalletStatus.LOADED);

        this.palletPort.changeStatus(PALLET_ID, PalletStatus.IN_TRANSIT, operatorId);

        final ArgumentCaptor<PalletHistoryEntry> entry = ArgumentCaptor.forClass(PalletHistoryEntry.class);
        verify(this.palletHistoryRepository).append(entry.capture());
        assertThat(entry.getValue().eventType()).isEqualTo(PalletHistoryEventType.STATUS_CHANGED);
        assertThat(entry.getValue().details()).isEqualTo("LOADED -> IN_TRANSIT");
    }
}
