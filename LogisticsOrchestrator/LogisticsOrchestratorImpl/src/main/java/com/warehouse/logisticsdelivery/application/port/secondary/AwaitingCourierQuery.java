package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTimeRange;

public record AwaitingCourierQuery(DepartmentId destinationDepartmentId,
                                   CourierAssignmentDateField dateField,
                                   DeliveryTimeRange range,
                                   DeliveryAssignmentCursor after,
                                   int limit) {
}
