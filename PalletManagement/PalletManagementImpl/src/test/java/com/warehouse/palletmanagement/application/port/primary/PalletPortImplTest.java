package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.palletmanagement.application.service.PalletDocumentService;
import com.warehouse.palletmanagement.application.service.PalletGrossWeightService;
import com.warehouse.palletmanagement.application.service.PalletTransportService;
import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.application.port.secondary.CarrierServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletTransportRepository;
import com.warehouse.palletmanagement.domain.enumeration.PalletHistoryEventType;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;
import java.time.Instant;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.application.port.primary.command.AddPalletShipmentsCommand;
import com.warehouse.palletmanagement.application.port.primary.command.CreatePalletCommand;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.palletmanagement.application.port.secondary.DepartmentServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletDocumentRendererServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.model.PalletDocumentData;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentFile;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import org.mockito.ArgumentCaptor;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PalletPortImplTest {

    private PalletHandlingManagerServicePort palletHandlingManagerServicePort;
    private ShipmentServicePort shipmentServicePort;
    private PalletPortImpl palletPort;

    private DepartmentServicePort departmentServicePort;
    private PalletDocumentRendererServicePort palletDocumentRendererServicePort;
    private CarrierServicePort carrierServicePort;
    private PalletTransportRepository palletTransportRepository;

    @BeforeEach
    void setUp() {
        this.palletHandlingManagerServicePort = mock(PalletHandlingManagerServicePort.class);
        this.shipmentServicePort = mock(ShipmentServicePort.class);
        this.departmentServicePort = mock(DepartmentServicePort.class);
        this.palletDocumentRendererServicePort = mock(PalletDocumentRendererServicePort.class);
        this.carrierServicePort = mock(CarrierServicePort.class);
        this.palletTransportRepository = mock(PalletTransportRepository.class);
        when(this.palletTransportRepository.findCurrent(any(PalletId.class))).thenReturn(Optional.empty());
        this.palletPort = new PalletPortImpl(this.palletHandlingManagerServicePort, this.shipmentServicePort,
                this.carrierServicePort, new PalletTransportService(this.palletTransportRepository),
                new PalletGrossWeightService(this.palletHandlingManagerServicePort, this.shipmentServicePort),
                new PalletDocumentService(this.shipmentServicePort, this.departmentServicePort,
                        this.palletDocumentRendererServicePort));
    }

    @Test
    void shouldAddPalletizableShipments() {
        final List<ShipmentId> shipmentIds = List.of(new ShipmentId(1L), new ShipmentId(2L));
        when(this.shipmentServicePort.findShipment(new ShipmentId(1L))).thenReturn(Optional.of(
                new PalletShipment(new ShipmentId(1L), ShipmentStatus.CREATED, new BigDecimal("12.5"))));
        when(this.shipmentServicePort.findShipment(new ShipmentId(2L))).thenReturn(Optional.of(
                new PalletShipment(new ShipmentId(2L), ShipmentStatus.PREPARED, new BigDecimal("0.75"))));
        when(this.palletHandlingManagerServicePort.addShipments(PalletFixture.PALLET_ID, shipmentIds))
                .thenReturn(PalletFixture.readyPallet(1L, 2L));
        when(this.palletHandlingManagerServicePort.changeGrossWeight(any(), any()))
                .thenReturn(PalletFixture.readyPallet(1L, 2L));

        assertThat(this.palletPort.addShipments(new AddPalletShipmentsCommand(PalletFixture.PALLET_ID, shipmentIds))
                .shipmentIds()).hasSize(2);
        final ArgumentCaptor<PalletWeight> weight = ArgumentCaptor.forClass(PalletWeight.class);
        verify(this.palletHandlingManagerServicePort).changeGrossWeight(eq(PalletFixture.PALLET_ID), weight.capture());
        assertThat(weight.getValue().value()).isEqualByComparingTo("13.25");
        assertThat(weight.getValue().unit()).isEqualTo("KG");
    }

    @Test
    void shouldRejectDeliveredShipment() {
        when(this.shipmentServicePort.findShipment(new ShipmentId(1L))).thenReturn(Optional.of(
                new PalletShipment(new ShipmentId(1L), ShipmentStatus.DELIVERY, BigDecimal.ONE)));

        assertThatThrownBy(() -> this.palletPort.createPallet(new CreatePalletCommand(List.of(new ShipmentId(1L)))))
                .isInstanceOfSatisfying(PalletRequestRejectedException.class,
                        exception -> assertThat(exception.getCode()).isEqualTo("SHIPMENT_NOT_PALLETIZABLE"));
        verify(this.palletHandlingManagerServicePort, never()).createPallet(any());
    }

    @Test
    void shouldRejectMissingShipment() {
        when(this.shipmentServicePort.findShipment(new ShipmentId(9L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.palletPort.addShipments(
                new AddPalletShipmentsCommand(PalletFixture.PALLET_ID, List.of(new ShipmentId(9L)))))
                .isInstanceOf(PalletRequestRejectedException.class);
        verify(this.palletHandlingManagerServicePort, never()).addShipments(any(), any());
    }

    @Test
    void shouldKeepPalletWhenWeightUpdateFailsBecausePalletServiceIsDown() {
        when(this.palletHandlingManagerServicePort.removeShipment(PalletFixture.PALLET_ID, new ShipmentId(1L)))
                .thenReturn(PalletFixture.readyPallet(2L));
        when(this.shipmentServicePort.findShipment(new ShipmentId(2L))).thenReturn(Optional.of(
                new PalletShipment(new ShipmentId(2L), ShipmentStatus.CREATED, BigDecimal.TEN)));
        when(this.palletHandlingManagerServicePort.changeGrossWeight(any(), any()))
                .thenThrow(new PalletServiceUnavailableException("unavailable"));

        assertThat(this.palletPort.removeShipment(PalletFixture.PALLET_ID, new ShipmentId(1L)).shipmentIds())
                .containsExactly(new ShipmentId(2L));
    }

    @Test
    void shouldRejectNonPositiveMaxWeight() {
        assertThatThrownBy(() -> this.palletPort.defineMaxGrossWeight(PalletFixture.PALLET_ID, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        verify(this.palletHandlingManagerServicePort, never()).defineMaxGrossWeight(any(), any());
    }

    @Test
    void shouldRejectStatusesThatAreNotSetManually() {
        assertThatThrownBy(() -> this.palletPort.changeStatus(PalletFixture.PALLET_ID, PalletStatus.LOADED))
                .isInstanceOf(PalletRequestRejectedException.class);
        verify(this.palletHandlingManagerServicePort, never()).changeStatus(any(), any());
    }

    @Test
    void shouldRequireCarrierBeforeDeparture() {
        assertThatThrownBy(() -> this.palletPort.changeStatus(PalletFixture.PALLET_ID, PalletStatus.IN_TRANSIT))
                .isInstanceOf(PalletRequestRejectedException.class)
                .hasMessageContaining("Carrier");
        verify(this.palletHandlingManagerServicePort, never()).changeStatus(any(), any());
    }

    @Test
    void shouldChangeTransportStatus() {
        when(this.palletTransportRepository.findCurrent(PalletFixture.PALLET_ID)).thenReturn(Optional.of(CARRIER));
        when(this.palletHandlingManagerServicePort.changeStatus(PalletFixture.PALLET_ID, PalletStatus.IN_TRANSIT))
                .thenReturn(PalletFixture.pallet(PalletStatus.IN_TRANSIT, false, null, 1L));

        assertThat(this.palletPort.changeStatus(PalletFixture.PALLET_ID, PalletStatus.IN_TRANSIT).palletStatus())
                .isEqualTo(PalletStatus.IN_TRANSIT);
    }

    @Test
    void shouldBuildTransportDocumentFromPalletShipmentsAndDepartments() {
        final PalletDetails base = PalletFixture.readyPallet(1L, 2L);
        final PalletDetails pallet = new PalletDetails(base.palletId(), base.trackingNumber(), base.palletStatus(),
                base.storageStatus(), base.shipmentIds(), false, true, null, null, null, new DepartmentId(10L),
                new DepartmentId(20L), "S-1", base.created(), base.modified(), null, null);
        final PalletTrackingNumber trackingNumber = pallet.trackingNumber();
        when(this.palletHandlingManagerServicePort.getByTrackingNumber(trackingNumber)).thenReturn(pallet);
        when(this.shipmentServicePort.describeShipment(new ShipmentId(1L))).thenReturn(Optional.of(
                new PalletDocumentShipment(new ShipmentId(1L), "FTM-1", ShipmentStatus.CREATED, "Kraków", "30-001",
                        new BigDecimal("2.5"))));
        when(this.shipmentServicePort.describeShipment(new ShipmentId(2L))).thenReturn(Optional.empty());
        when(this.departmentServicePort.findDepartment(new DepartmentId(10L))).thenReturn(Optional.of(
                new PalletDepartment(new DepartmentId(10L), "KT1", "Katowice", "Mariacka 1", "40-001", "PL")));
        when(this.departmentServicePort.findDepartment(new DepartmentId(20L))).thenReturn(Optional.empty());
        when(this.palletDocumentRendererServicePort.renderTransportDocument(any())).thenReturn(new byte[]{1, 2});

        final PalletDocumentFile file = this.palletPort.generateTransportDocument(trackingNumber);

        assertThat(file.content()).containsExactly(1, 2);
        assertThat(file.fileName()).startsWith("list-przewozowy-").endsWith(".pdf");
        final ArgumentCaptor<PalletDocumentData> data = ArgumentCaptor.forClass(PalletDocumentData.class);
        verify(this.palletDocumentRendererServicePort).renderTransportDocument(data.capture());
        assertThat(data.getValue().origin().departmentCode()).isEqualTo("KT1");
        assertThat(data.getValue().destination().departmentCode()).isEqualTo("20");
        assertThat(data.getValue().shipments()).hasSize(2);
        assertThat(data.getValue().shipments().get(1).trackingNumber()).isNull();
        assertThat(data.getValue().cargoWeightInKilograms()).isEqualByComparingTo("2.5");
    }

    private static final PalletCarrier CARRIER = new PalletCarrier(new CarrierId(1L), "Trans-Pol", TransportType.OWN_FLEET,
            new DriverId(5L), "Jan Kowalski", "500600700", new VehicleId(7L), "SK 12345", null, Instant.parse("2026-10-18T09:00:00Z"), "jan");

    @Test
    void shouldAssignCarrierWithDriverAndVehicleFromCatalogue() {
        when(this.palletHandlingManagerServicePort.getPallet(PalletFixture.PALLET_ID))
                .thenReturn(PalletFixture.readyPallet(1L));
        when(this.carrierServicePort.findActiveCarrier(new CarrierId(1L))).thenReturn(Optional.of(new TransportCarrier(new CarrierId(1L), "Trans-Pol",
                TransportType.EXTERNAL_CARRIER, true,
                List.of(new TransportCarrier.Driver(new DriverId(5L), "Jan Kowalski", "500600700", true)),
                List.of(new TransportCarrier.Vehicle(new VehicleId(7L), "SK 12345", "SK 999", true)))));
        when(this.palletTransportRepository.append(any(), any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(2));

        final PalletDetails pallet = this.palletPort.assignCarrier(PalletFixture.PALLET_ID,
                new PalletCarrierAssignment(new CarrierId(1L), null, new DriverId(5L), null, null, new VehicleId(7L),
                        null, null));

        final PalletCarrier carrier = pallet.carrier();
        assertThat(carrier.carrierName()).isEqualTo("Trans-Pol");
        assertThat(carrier.transportType()).isEqualTo(TransportType.EXTERNAL_CARRIER);
        assertThat(carrier.driverName()).isEqualTo("Jan Kowalski");
        assertThat(carrier.vehicleRegistrationNumber()).isEqualTo("SK 12345");
        assertThat(carrier.trailerRegistrationNumber()).isEqualTo("SK 999");
    }

    @Test
    void shouldNotChangeCarrierOfPalletInTransit() {
        when(this.palletHandlingManagerServicePort.getPallet(PalletFixture.PALLET_ID))
                .thenReturn(PalletFixture.pallet(PalletStatus.IN_TRANSIT, false, null, 1L));

        assertThatThrownBy(() -> this.palletPort.assignCarrier(PalletFixture.PALLET_ID,
                new PalletCarrierAssignment(new CarrierId(1L), null, null, null, null, null, "SK 1", null)))
                .isInstanceOf(PalletRequestRejectedException.class);
        verify(this.palletTransportRepository, never()).append(any(), any(), any());
    }

    @Test
    void shouldRejectInactiveOrUnknownCarrier() {
        when(this.palletHandlingManagerServicePort.getPallet(PalletFixture.PALLET_ID))
                .thenReturn(PalletFixture.readyPallet(1L));
        when(this.carrierServicePort.findActiveCarrier(new CarrierId(2L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.palletPort.assignCarrier(PalletFixture.PALLET_ID,
                new PalletCarrierAssignment(new CarrierId(2L), null, null, null, null, null, "SK 1", null)))
                .isInstanceOf(PalletRequestRejectedException.class)
                .hasMessageContaining("not available");
        verify(this.palletTransportRepository, never()).append(any(), any(), any());
    }

    @Test
    void shouldRequireVehicleRegistration() {
        when(this.palletHandlingManagerServicePort.getPallet(PalletFixture.PALLET_ID))
                .thenReturn(PalletFixture.readyPallet(1L));
        when(this.carrierServicePort.findActiveCarrier(new CarrierId(1L))).thenReturn(Optional.of(new TransportCarrier(new CarrierId(1L), "Trans-Pol",
                TransportType.EXTERNAL_CARRIER, true, List.of(), List.of())));

        assertThatThrownBy(() -> this.palletPort.assignCarrier(PalletFixture.PALLET_ID,
                new PalletCarrierAssignment(new CarrierId(1L), null, null, null, null, null, null, null)))
                .isInstanceOf(PalletRequestRejectedException.class);
    }

    @Test
    void shouldMergeTransportAssignmentsIntoPalletHistory() {
        when(this.palletHandlingManagerServicePort.getHistory(PalletFixture.PALLET_ID)).thenReturn(List.of(
                new PalletHistoryEntry(PalletHistoryEventType.CREATED, PalletStatus.CREATED,
                        Instant.parse("2026-10-18T08:00:00Z"), null, "jan", null, null)));
        when(this.palletTransportRepository.findHistory(PalletFixture.PALLET_ID)).thenReturn(List.of(CARRIER));

        final List<PalletHistoryEntry> history = this.palletPort.getHistory(PalletFixture.PALLET_ID);

        assertThat(history).extracting(PalletHistoryEntry::eventType)
                .containsExactly(PalletHistoryEventType.CARRIER_ASSIGNED, PalletHistoryEventType.CREATED);
        assertThat(history.get(0).details()).contains("Trans-Pol", "SK 12345", "Jan Kowalski");
    }
}
