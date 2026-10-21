package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.dto.ShipmentDeliveryDto;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.qrcode.domain.port.secondary.DeliveryServicePort;
import com.warehouse.qrcode.domain.vo.ShipmentId;

public class DeliveryServiceAdapter implements DeliveryServicePort {

    private final DeliveryApiService deliveryApiService;

    private final DepartmentApiService departmentApiService;

    public DeliveryServiceAdapter(final DeliveryApiService deliveryApiService,
                                  final DepartmentApiService departmentApiService) {
        this.deliveryApiService = deliveryApiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public String findDestinationDepartmentCode(final ShipmentId shipmentId) {
        final ShipmentDeliveryDto delivery = deliveryApiService.getShipmentDelivery(
                new com.warehouse.commonassets.identificator.ShipmentId(shipmentId.getValue()));
        if (delivery.destinationDepartmentId() == null) {
            return null;
        }
        final DepartmentDto department = departmentApiService.getDepartmentById(delivery.destinationDepartmentId());
        return department != null ? department.departmentCode() : null;
    }
}
