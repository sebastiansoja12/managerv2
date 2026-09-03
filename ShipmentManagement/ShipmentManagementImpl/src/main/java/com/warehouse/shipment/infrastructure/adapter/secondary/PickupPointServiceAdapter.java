package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.api.PickupPointApiService;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionDto;
import com.warehouse.shipment.application.port.secondary.PickupPointServicePort;

import java.util.Optional;

public class PickupPointServiceAdapter implements PickupPointServicePort {

    private final PickupPointApiService pickupPointApiService;

    public PickupPointServiceAdapter(final PickupPointApiService pickupPointApiService) {
        this.pickupPointApiService = pickupPointApiService;
    }

    @Override
    public Optional<DepartmentId> findDepartmentId(final PickupPointId pickupPointId) {
        return pickupPointApiService.getById(new PickupPointIdDto(pickupPointId.value()))
                .map(PickupPointSelectionDto::departmentId)
                .map(departmentId -> new DepartmentId(departmentId.value()));
    }
}
