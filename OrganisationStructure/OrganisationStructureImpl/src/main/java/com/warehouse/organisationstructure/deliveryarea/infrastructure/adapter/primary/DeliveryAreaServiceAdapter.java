package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.api.DeliveryAreaApiService;
import com.warehouse.organisationstructure.deliveryarea.api.dto.DeliveryLocationDto;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.DeliveryAreaPort;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;

import java.util.Optional;

public class DeliveryAreaServiceAdapter implements DeliveryAreaApiService {

    private final DeliveryAreaPort deliveryAreaPort;

    public DeliveryAreaServiceAdapter(final DeliveryAreaPort deliveryAreaPort) {
        this.deliveryAreaPort = deliveryAreaPort;
    }

    @Override
    public Optional<DepartmentId> findServingDepartment(final DeliveryLocationDto location) {
        if (location == null) {
            return Optional.empty();
        }
        return deliveryAreaPort.findServingDepartment(new GeoPoint(location.latitude(), location.longitude()));
    }
}
