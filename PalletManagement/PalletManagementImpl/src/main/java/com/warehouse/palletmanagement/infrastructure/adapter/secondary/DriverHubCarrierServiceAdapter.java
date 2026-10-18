package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.api.CarrierApiService;
import com.warehouse.driverhub.api.dto.CarrierDto;
import com.warehouse.palletmanagement.application.port.secondary.CarrierServicePort;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;

import java.util.Arrays;
import java.util.Optional;

public class DriverHubCarrierServiceAdapter implements CarrierServicePort {

    private final CarrierApiService carrierApiService;

    public DriverHubCarrierServiceAdapter(final CarrierApiService carrierApiService) {
        this.carrierApiService = carrierApiService;
    }

    @Override
    public Optional<TransportCarrier> findActiveCarrier(final CarrierId carrierId) {
        return this.carrierApiService.findActiveCarrier(carrierId.getValue()).map(DriverHubCarrierServiceAdapter::map);
    }

    private static TransportCarrier map(final CarrierDto carrier) {
        return new TransportCarrier(CarrierId.of(carrier.carrierId()), carrier.name(), transportType(carrier.defaultTransportType()),
                carrier.active(),
                carrier.drivers().stream()
                        .map(driver -> new TransportCarrier.Driver(DriverId.of(driver.driverId()), driver.fullName(), driver.phone(),
                                driver.active()))
                        .toList(),
                carrier.vehicles().stream()
                        .map(vehicle -> new TransportCarrier.Vehicle(new VehicleId(vehicle.vehicleId()), vehicle.registrationNumber(),
                                vehicle.trailerRegistrationNumber(), vehicle.active()))
                        .toList());
    }

    private static TransportType transportType(final String value) {
        return Arrays.stream(TransportType.values())
                .filter(type -> type.name().equals(value))
                .findFirst()
                .orElse(TransportType.EXTERNAL_CARRIER);
    }
}
