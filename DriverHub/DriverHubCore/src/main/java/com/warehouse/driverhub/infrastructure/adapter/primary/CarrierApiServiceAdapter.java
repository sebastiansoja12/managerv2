package com.warehouse.driverhub.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.driverhub.api.CarrierApiService;
import com.warehouse.driverhub.api.dto.CarrierDto;
import com.warehouse.driverhub.api.dto.CarrierVehicleCreateDto;
import com.warehouse.driverhub.api.dto.CarrierVehicleDto;
import com.warehouse.driverhub.application.port.primary.CarrierPort;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.infrastructure.adapter.primary.mapper.CarrierApiMapper;

import java.util.List;
import java.util.Optional;

public class CarrierApiServiceAdapter implements CarrierApiService {

    private final CarrierPort carrierPort;

    public CarrierApiServiceAdapter(final CarrierPort carrierPort) {
        this.carrierPort = carrierPort;
    }

    @Override
    public List<CarrierDto> findCarriers(final boolean activeOnly) {
        return this.carrierPort.findCarriers(activeOnly).stream().map(CarrierApiMapper::map).toList();
    }

    @Override
    public Optional<CarrierDto> findActiveCarrier(final Long carrierId) {
        return this.carrierPort.findActiveCarrier(CarrierId.of(carrierId)).map(CarrierApiMapper::map);
    }

    @Override
    public CarrierVehicleDto addVehicle(final Long carrierId, final CarrierVehicleCreateDto vehicle) {
        final CarrierDto carrier = CarrierApiMapper.map(
                this.carrierPort.addVehicle(CarrierId.of(carrierId), CarrierApiMapper.map(vehicle)));
        return carrier.vehicles().stream()
                .filter(added -> added.registrationNumber().equalsIgnoreCase(vehicle.registrationNumber().trim()))
                .findFirst()
                .orElseThrow(() -> DriverHubException.invalid("Vehicle " + vehicle.registrationNumber()
                        + " was not added"));
    }
}
