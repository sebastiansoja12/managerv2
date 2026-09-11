package com.warehouse.driverhub.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.driverhub.domain.enumeration.TransportType;
import com.warehouse.driverhub.domain.enumeration.VehicleType;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.domain.vo.CarrierChange;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.domain.vo.VehicleChange;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarrierTest {

    @Test
    void shouldRegisterCarrierWithDriverAndVehicle() {
        final Carrier carrier = Carrier.register(new OperatorId(1L), " Trans-Pol ", TaxId.of(" 1234567890 "), null, null,
                null);

        carrier.addDriver("Jan", "Kowalski", "500600700", null);
        carrier.addVehicle(" sk  12345 ", " Volvo ", "FH16", VehicleType.TRUCK, "sk 999", null);

        assertThat(carrier.getCarrierId()).isNotNull();
        assertThat(carrier.getDrivers().get(0).getDriverId()).isNotNull();
        assertThat(carrier.getVehicles().get(0).getVehicleId()).isNotNull();
        assertThat(carrier.getName()).isEqualTo("Trans-Pol");
        assertThat(carrier.getTaxId().getValue()).isEqualTo("1234567890");
        assertThat(carrier.getDefaultTransportType()).isEqualTo(TransportType.EXTERNAL_CARRIER);
        assertThat(carrier.isActive()).isTrue();
        assertThat(carrier.getDrivers()).hasSize(1);
        assertThat(carrier.getVehicles().get(0).getRegistrationNumber()).isEqualTo("SK 12345");
        assertThat(carrier.getVehicles().get(0).getTrailerRegistrationNumber()).isEqualTo("SK 999");
        assertThat(carrier.getVehicles().get(0).getBrand()).isEqualTo("Volvo");
        assertThat(carrier.getVehicles().get(0).getModel()).isEqualTo("FH16");
    }

    @Test
    void shouldRejectDuplicatedVehicleAndBlankName() {
        final Carrier carrier = Carrier.register(new OperatorId(1L), "Trans-Pol", null, null, null, null);
        carrier.addVehicle("SK 1", null, null, VehicleType.VAN, null, null);

        assertThatThrownBy(() -> carrier.addVehicle("sk 1", null, null, VehicleType.VAN, null, null))
                .isInstanceOf(DriverHubException.class);
        assertThatThrownBy(() -> Carrier.register(new OperatorId(1L), " ", null, null, null, null))
                .isInstanceOf(DriverHubException.class);
    }

    @Test
    void shouldRequireOperator() {
        assertThatThrownBy(() -> Carrier.register(null, "Trans-Pol", null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldChangeSingleFields() {
        final Carrier carrier = Carrier.register(new OperatorId(1L), "Trans-Pol", null, "1", null, null);

        carrier.apply(new CarrierChange.Name(" Trans-Pol 2 "));
        carrier.apply(new CarrierChange.Phone(" "));
        carrier.apply(new CarrierChange.DefaultTransport(TransportType.OWN_FLEET));
        carrier.apply(new CarrierChange.Active(false));

        assertThat(carrier.getName()).isEqualTo("Trans-Pol 2");
        assertThat(carrier.getPhone()).isNull();
        assertThat(carrier.getDefaultTransportType()).isEqualTo(TransportType.OWN_FLEET);
        assertThat(carrier.isActive()).isFalse();
        assertThatThrownBy(() -> carrier.apply(new CarrierChange.Name(" ")))
                .isInstanceOf(DriverHubException.class);
    }

    @Test
    void shouldChangeDriverAndVehicleFields() {
        final Carrier carrier = new Carrier(new CarrierId(1L), new OperatorId(1L), "Trans-Pol", null, null, null, null,
                true, null,
                List.of(new CarrierDriver(new DriverId(5L), "Jan", "Kowalski", null, null, true)),
                List.of(new CarrierVehicle(new VehicleId(7L), "SK 1", null, null, VehicleType.TRUCK, null, null, true),
                        new CarrierVehicle(new VehicleId(8L), "SK 2", null, null, VehicleType.VAN, null, null, true)));

        carrier.changeDriver(new DriverId(5L), new DriverChange.Phone("500600700"));
        carrier.changeDriver(new DriverId(5L), new DriverChange.Active(false));
        carrier.changeVehicle(new VehicleId(7L), new VehicleChange.TrailerRegistrationNumber("sk 99"));

        assertThat(carrier.driver(new DriverId(5L)).getPhone()).isEqualTo("500600700");
        assertThat(carrier.driver(new DriverId(5L)).isActive()).isFalse();
        assertThat(carrier.vehicle(new VehicleId(7L)).getTrailerRegistrationNumber()).isEqualTo("SK 99");
        assertThatThrownBy(() -> carrier.changeVehicle(new VehicleId(7L), new VehicleChange.RegistrationNumber("sk 2")))
                .isInstanceOf(DriverHubException.class);
    }
}
