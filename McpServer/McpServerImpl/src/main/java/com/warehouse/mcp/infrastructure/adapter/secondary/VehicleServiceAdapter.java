package com.warehouse.mcp.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.api.CarrierApiService;
import com.warehouse.driverhub.api.dto.CarrierVehicleCreateDto;
import com.warehouse.mcp.application.port.secondary.VehicleServicePort;
import com.warehouse.mcp.domain.vo.VehicleDraft;

public class VehicleServiceAdapter implements VehicleServicePort {

    private final CarrierApiService carrierApiService;

    public VehicleServiceAdapter(final CarrierApiService carrierApiService) {
        this.carrierApiService = carrierApiService;
    }

    @Override
    public VehicleId create(final VehicleDraft vehicle) {
        return new VehicleId(this.carrierApiService.addVehicle(vehicle.carrierId().value(),
                new CarrierVehicleCreateDto(vehicle.registrationNumber(), vehicle.brand(), vehicle.model(),
                        vehicle.vehicleType(), vehicle.maxLoadKg())).vehicleId());
    }
}
