package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryReturnRequestMapper {

    default DeliveryReturnRequest map(final TerminalRequest request) {
        return new DeliveryReturnRequest(map(request.getProcessType()), map(request.getDevice()), Collections.emptyList());
    }

    List<DeliveryReturnDetails> map(final List<DeliveryReturnDetail> deliveryReturnRequests);

    @Mapping(target = "departmentCode.value", source = "departmentCode")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "shipmentId.value", source = "shipmentId")
    DeliveryReturnDetails map(final DeliveryReturnDetail deliveryReturnDetail);

    @Mapping(target = "deviceId.value", source = "deviceId")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(final Device device);

    ProcessType map(final com.warehouse.terminal.enumeration.ProcessType processType);

    DeliveryReturnRequest mapToDeliveryReturnRequest(final DeliveryReturnRequestDto deliveryReturnRequest);

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "username", source = "username.value")
    DeviceInformation map(final DeviceInformationDto deviceInformation);
}
