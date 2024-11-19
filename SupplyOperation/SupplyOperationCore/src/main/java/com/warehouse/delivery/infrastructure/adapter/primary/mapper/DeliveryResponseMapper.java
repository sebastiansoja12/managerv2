package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.delivery.domain.model.Response;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.response.TerminalResponse;

@Mapper
public interface DeliveryResponseMapper {
    TerminalResponse map(final Response response);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    @Mapping(target = "shipmentId", source = "shipmentId.value")
    @Mapping(target = "newShipmentId", source = "newShipmentId.value")
    @Mapping(target = "rejectReason", source = "rejectReason.value")
    DeliveryRejectResponse map(final com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse response);

}
