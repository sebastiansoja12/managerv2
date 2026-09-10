package com.warehouse.palletmanagement.domain.service;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PalletTransportPolicyTest {

    private static final TransportCarrier CARRIER = new TransportCarrier(new CarrierId(1L), "Trans-Pol",
            TransportType.EXTERNAL_CARRIER, true,
            List.of(new TransportCarrier.Driver(new DriverId(5L), "Jan Kowalski", "500600700", true)),
            List.of(new TransportCarrier.Vehicle(new VehicleId(7L), "SK 12345", "SK 999", true)));

    @Test
    void shouldPreferTypedValuesOverCatalogue() {
        final PalletCarrier carrier = PalletTransportPolicy.assign(CARRIER, new PalletCarrierAssignment(
                new CarrierId(1L), TransportType.OWN_FLEET, null, " Adam Nowak ", null, null, " sk 777 ", null),
                Instant.now());

        assertThat(carrier.transportType()).isEqualTo(TransportType.OWN_FLEET);
        assertThat(carrier.driverName()).isEqualTo("Adam Nowak");
        assertThat(carrier.vehicleRegistrationNumber()).isEqualTo("SK 777");
        assertThat(carrier.vehicleId()).isNull();
    }

    @Test
    void shouldRejectDriverOutsideActiveCatalogue() {
        assertThatThrownBy(() -> PalletTransportPolicy.assign(CARRIER, new PalletCarrierAssignment(
                new CarrierId(1L), null, new DriverId(99L), null, null, new VehicleId(7L), null, null), Instant.now()))
                .isInstanceOf(PalletRequestRejectedException.class);
    }

    @Test
    void shouldAllowOnlyTransportStatusesToBeSetManually() {
        assertThatThrownBy(() -> PalletTransportPolicy.ensureManualStatusChange(PalletStatus.READY, true))
                .isInstanceOf(PalletRequestRejectedException.class);
        assertThatThrownBy(() -> PalletTransportPolicy.ensureManualStatusChange(PalletStatus.IN_TRANSIT, false))
                .isInstanceOf(PalletRequestRejectedException.class);
        assertThatCode(() -> PalletTransportPolicy.ensureManualStatusChange(PalletStatus.DELIVERED, false))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldLockCarrierWhenPalletDeparted() {
        assertThatThrownBy(() -> PalletTransportPolicy.ensureCarrierChangeable(
                PalletFixture.pallet(PalletStatus.IN_TRANSIT, false, null, 1L)))
                .isInstanceOf(PalletRequestRejectedException.class);
        assertThatCode(() -> PalletTransportPolicy.ensureCarrierChangeable(PalletFixture.readyPallet(1L)))
                .doesNotThrowAnyException();
    }
}
