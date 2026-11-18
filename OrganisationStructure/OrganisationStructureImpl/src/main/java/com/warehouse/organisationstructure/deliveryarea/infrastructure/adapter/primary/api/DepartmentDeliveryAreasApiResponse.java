package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import java.util.List;

public record DepartmentDeliveryAreasApiResponse(Long departmentId,
                                                 String departmentCode,
                                                 List<DeliveryAreaApiResponse> areas) {
}
