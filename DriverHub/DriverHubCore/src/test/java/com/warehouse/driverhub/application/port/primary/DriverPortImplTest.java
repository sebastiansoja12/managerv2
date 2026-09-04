package com.warehouse.driverhub.application.port.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.driverhub.application.port.primary.command.DriverCommand;
import com.warehouse.driverhub.application.port.secondary.CarrierRepository;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.model.CarrierDriver;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.domain.vo.DriverDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DriverPortImplTest {

    private static final OperatorId OPERATOR_ID = new OperatorId(1L);

    private CarrierRepository carrierRepository;
    private DriverPortImpl driverPort;

    @BeforeEach
    void setUp() {
        this.carrierRepository = mock(CarrierRepository.class);
        this.driverPort = new DriverPortImpl(this.carrierRepository, () -> OPERATOR_ID);
    }

    @Test
    void shouldAddDriverToCarrier() {
        when(this.carrierRepository.findById(new CarrierId(1L), OPERATOR_ID)).thenReturn(Optional.of(carrier(List.of())));
        when(this.carrierRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final DriverDetails details = this.driverPort.addDriver(new CarrierId(1L),
                new DriverCommand("Jan", "Kowalski", "500", null));

        assertThat(details.driver().getDriverId()).isNotNull();
        assertThat(details.driver().getLastName()).isEqualTo("Kowalski");
        assertThat(details.carrierName()).isEqualTo("Trans-Pol");
    }

    @Test
    void shouldChangeSingleDriverField() {
        final Carrier carrier = carrier(List.of(new CarrierDriver(new DriverId(4L), "Jan", "Kowalski", null, null, true)));
        when(this.carrierRepository.findByDriverId(new DriverId(4L), OPERATOR_ID)).thenReturn(Optional.of(carrier));
        when(this.carrierRepository.save(carrier)).thenReturn(carrier);

        final DriverDetails details = this.driverPort.changeDriver(new DriverId(4L), new DriverChange.LicenseNumber("ABC"));

        assertThat(details.driver().getLicenseNumber()).isEqualTo("ABC");
    }

    @Test
    void shouldListOnlyActiveDrivers() {
        when(this.carrierRepository.findAll(OPERATOR_ID, false)).thenReturn(List.of(carrier(List.of(
                new CarrierDriver(new DriverId(3L), "Adam", "Nowak", null, null, false),
                new CarrierDriver(new DriverId(4L), "Jan", "Kowalski", null, null, true)))));

        assertThat(this.driverPort.findDrivers(true)).extracting(details -> details.driver().getDriverId())
                .containsExactly(new DriverId(4L));
        assertThat(this.driverPort.findDrivers(false)).hasSize(2);
    }

    @Test
    void shouldRejectUnknownDriver() {
        when(this.carrierRepository.findByDriverId(new DriverId(9L), OPERATOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.driverPort.changeDriver(new DriverId(9L), new DriverChange.Active(false)))
                .isInstanceOf(DriverHubException.class);
    }

    private static Carrier carrier(final List<CarrierDriver> drivers) {
        return new Carrier(new CarrierId(1L), OPERATOR_ID, "Trans-Pol", null, null, null, null, true, null,
                new ArrayList<>(drivers), List.of());
    }
}
