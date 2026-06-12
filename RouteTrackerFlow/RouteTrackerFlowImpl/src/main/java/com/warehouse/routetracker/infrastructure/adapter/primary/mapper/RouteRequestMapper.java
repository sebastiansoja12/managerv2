package com.warehouse.routetracker.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.model.DeliveryReturnRequest;
import com.warehouse.routetracker.domain.vo.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.*;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn.DeliveryReturnRequestDto;

@Mapper
public interface RouteRequestMapper {

    ShipmentId map(ShipmentIdDto id);

    DeviceIdInformation map(TerminalIdRequestDto zebraIdInformation);

    DeviceVersionInformation map(TerminalVersionRequestDto versionInformation);

    ErrorInformation map(ErrorInformationRequestDto errorInformation);

    TerminalRequest map(TerminalRequestDto terminalRequest);

    ReturnTrackRequest map(ReturnTrackRequestDto returnTrackRequest);

    DeliveryReturnRequest map(DeliveryReturnRequestDto deliveryReturnRequest);

    SupplierIdRequest map(SupplierIdRequestDto supplierIdRequest);

    DepartmentIdRequest map(DepartmentIdRequestDto departmentIdRequest);

    UserIdRequest map(UserIdRequestDto userIdRequest);

    DescriptionRequest map(DescriptionRequestDto descriptionRequest);

    ZebraInitializeRequest map(ZebraInitializeRequestDto initializeRequest);

    DeliveryStatusRequest map(DeliveryStatusRequestDto deliveryStatusRequest);

    default TerminalId map(final TerminalIdDto terminalId) {
        return terminalId != null ? new TerminalId(terminalId.getValue()) : null;
    }

    default UserId map(final UserIdDto userId) {
        return userId != null ? new UserId(userId.value()) : null;
    }

    default SupplierId map(final SupplierIdDto supplierId) {
        return supplierId != null ? new SupplierId(supplierId.value()) : null;
    }

    default DepartmentId map(final DepartmentIdDto departmentId) {
        return departmentId != null ? new DepartmentId(departmentId.value()) : null;
    }
}
