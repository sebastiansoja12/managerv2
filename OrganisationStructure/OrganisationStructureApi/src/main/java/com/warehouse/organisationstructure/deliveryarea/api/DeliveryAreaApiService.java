package com.warehouse.organisationstructure.deliveryarea.api;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.api.dto.DeliveryLocationDto;

import java.util.Optional;

public interface DeliveryAreaApiService {

    Optional<DepartmentId> findServingDepartment(final DeliveryLocationDto location);
}
