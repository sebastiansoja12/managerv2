package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.DepartmentCodeApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReasonCodeApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnTokenApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.UserIdApi;

public final class ReturnResponseMapper {

    private ReturnResponseMapper() {
    }

    public static ShipmentReturnDetails map(final ReturnPackageApi response) {
        return new ShipmentReturnDetails(
                new ReturnId(response.returnPackageId().value()),
                new ShipmentId(response.shipmentId().value()),
                response.reason(),
                ReturnStatus.valueOf(response.returnStatus()),
                value(response.returnToken()),
                map(response.assignedDepartmentCode()),
                map(response.returnedDepartmentCode()),
                map(response.assignedTo()),
                map(response.processedBy()),
                map(response.reasonCode()),
                response.operatorId(),
                response.createdAt(),
                response.updatedAt());
    }

    public static ShipmentReturnPage map(final ReturnPageApi response) {
        return new ShipmentReturnPage(
                response.content().stream().map(ReturnResponseMapper::map).toList(),
                response.page(),
                response.size(),
                response.totalElements(),
                response.totalPages());
    }

    private static String value(final ReturnTokenApi returnToken) {
        return returnToken == null ? null : returnToken.value();
    }

    private static DepartmentCode map(final DepartmentCodeApi departmentCode) {
        return departmentCode == null ? null : new DepartmentCode(departmentCode.value());
    }

    private static UserId map(final UserIdApi userId) {
        return userId == null ? null : new UserId(userId.value());
    }

    private static ReasonCode map(final ReasonCodeApi reasonCode) {
        return reasonCode == null ? null : ReasonCode.valueOf(reasonCode.value());
    }
}
