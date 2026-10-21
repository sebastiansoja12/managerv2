package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.enumeration.Unit;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.exception.PalletNotFoundException;
import com.warehouse.pallet.domain.exception.ShipmentAlreadyOnPalletException;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PalletStorageServiceImplTest {

    private static final OperatorId OPERATOR_ID = new OperatorId(1L);

    private PalletRepository palletRepository;
    private PalletTrackingNumberGenerator trackingNumberGenerator;
    private PalletStorageServiceImpl palletStorageService;

    @BeforeEach
    void setUp() {
        this.palletRepository = mock(PalletRepository.class);
        this.trackingNumberGenerator = mock(PalletTrackingNumberGenerator.class);
        this.palletStorageService = new PalletStorageServiceImpl(this.palletRepository, this.trackingNumberGenerator);
    }

    @Test
    void shouldGenerateNextUniqueTrackingNumber() {
        final TrackingNumber taken = new TrackingNumber("PAL1");
        final TrackingNumber free = new TrackingNumber("PAL2");
        when(this.trackingNumberGenerator.generate()).thenReturn(taken, free);
        when(this.palletRepository.existsByTrackingNumber(taken)).thenReturn(true);
        when(this.palletRepository.existsByTrackingNumber(free)).thenReturn(false);

        assertThat(this.palletStorageService.nextTrackingNumber()).isEqualTo(free);
    }

    @Test
    void shouldGenerateTrackingNumbersWithPrefix() {
        assertThat(new PalletTrackingNumberGenerator().generate().value()).matches("PAL\\d{12}");
    }

    @Test
    void shouldCreatePalletWithShipments() {
        when(this.palletRepository.findActivePalletIdContaining(any())).thenReturn(Optional.empty());

        final Pallet pallet = this.palletStorageService.createPalletWithShipments(new PalletId("p1"),
                new TrackingNumber("PAL1"), OPERATOR_ID, new DepartmentId(10L), Set.of(new ShipmentId(1L)));

        assertThat(pallet.getPalletStatus()).isEqualTo(PalletStatus.READY);
        assertThat(pallet.getOriginDepartmentId()).isEqualTo(new DepartmentId(10L));
        verify(this.palletRepository).createOrUpdate(pallet);
    }

    @Test
    void shouldRejectShipmentAlreadyOnAnotherActivePallet() {
        when(this.palletRepository.findActivePalletIdContaining(new ShipmentId(1L)))
                .thenReturn(Optional.of(new PalletId("other")));

        assertThatThrownBy(() -> this.palletStorageService.createPalletWithShipments(new PalletId("p1"),
                new TrackingNumber("PAL1"), OPERATOR_ID, new DepartmentId(10L), Set.of(new ShipmentId(1L))))
                .isInstanceOf(ShipmentAlreadyOnPalletException.class);
        verify(this.palletRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldAllowAddingShipmentAlreadyOnSamePallet() {
        final Pallet pallet = Pallet.withShipments(new PalletId("p1"), new TrackingNumber("PAL1"), OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        when(this.palletRepository.findById(new PalletId("p1"))).thenReturn(pallet);
        when(this.palletRepository.findActivePalletIdContaining(new ShipmentId(1L)))
                .thenReturn(Optional.of(new PalletId("p1")));

        this.palletStorageService.addShipment(new PalletId("p1"), new ShipmentId(1L));

        assertThat(pallet.getShipmentIds()).hasSize(1);
    }

    @Test
    void shouldThrowWhenPalletNotFoundByTrackingNumber() {
        when(this.palletRepository.findByTrackingNumber(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.palletStorageService.findByTrackingNumber(new TrackingNumber("PAL404")))
                .isInstanceOf(PalletNotFoundException.class);
    }

    @Test
    void shouldNotSaveWhenPalletAlreadyLoaded() {
        final Pallet pallet = Pallet.withShipments(new PalletId("p1"), new TrackingNumber("PAL1"), OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        pallet.markLoaded();
        when(this.palletRepository.findById(new PalletId("p1"))).thenReturn(pallet);

        assertThat(this.palletStorageService.markLoaded(new PalletId("p1"))).isFalse();
        verify(this.palletRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldChangeWeightsAndReevaluateReadiness() {
        final Pallet pallet = Pallet.withShipments(new PalletId("p1"), new TrackingNumber("PAL1"), OPERATOR_ID,
                List.of(new ShipmentId(1L)));
        when(this.palletRepository.findById(new PalletId("p1"))).thenReturn(pallet);

        this.palletStorageService.defineMaxGrossWeight(new PalletId("p1"), new BigDecimal("1000"));
        final Pallet changed = this.palletStorageService.changeGrossWeight(new PalletId("p1"),
                new Weight(new BigDecimal("1.5"), Unit.T));

        assertThat(changed.getPalletStatus()).isEqualTo(PalletStatus.NOT_READY);
    }

    @Test
    void shouldChangeDepartments() {
        final Pallet pallet = Pallet.empty(new PalletId("p1"), new TrackingNumber("PAL1"), OPERATOR_ID);
        when(this.palletRepository.findById(new PalletId("p1"))).thenReturn(pallet);

        final Pallet changed = this.palletStorageService.changeDepartments(new PalletId("p1"),
                new DepartmentId(10L), new DepartmentId(20L));

        assertThat(changed.getOriginDepartmentId()).isEqualTo(new DepartmentId(10L));
        assertThat(changed.getDestinationDepartmentId()).isEqualTo(new DepartmentId(20L));
    }
}
