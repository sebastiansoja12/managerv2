package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.SupplierCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.delivery.domain.model.Response;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.response.TerminalResponse;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryResponseMapper {

    @Mapping(target = "device.username", source = "deviceInformation.username")
    @Mapping(target = "device.departmentCode", source = "deviceInformation.departmentCode.value")
    @Mapping(target = "device.deviceId", source = "deviceInformation.deviceId.value")
    TerminalResponse map(final Response response);

    String map(final SupplierCode supplierCode);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    @Mapping(target = "shipmentId", source = "shipmentId.value")
    @Mapping(target = "newShipmentId", source = "newShipmentId.value")
    @Mapping(target = "rejectReason", source = "rejectReason.value")
    DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response);

}
