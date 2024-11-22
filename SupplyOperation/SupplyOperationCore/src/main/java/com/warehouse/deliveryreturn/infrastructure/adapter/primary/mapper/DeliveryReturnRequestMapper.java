package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryReturnRequestMapper {

    default DeliveryReturnRequest map(final Request request) {
        final ProcessType processType = request.getProcessType();
        final DeviceInformation deviceInformation = map(request.getDeviceInformation());
        final List<DeliveryReturnDetails> deliveryReturnDetails = new ArrayList<>();
        return new DeliveryReturnRequest(processType, deviceInformation, deliveryReturnDetails);
    }

    default DeliveryReturnRequest map(TerminalRequest request) {
        return new DeliveryReturnRequest(map(request.getProcessType()), map(request.getDevice()),
                map(request.getDeliveryReturnDetails()));
    }

    List<DeliveryReturnDetails> map(List<DeliveryReturnDetail> deliveryReturnRequests);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "shipmentId.value", source = "shipmentId")
    DeliveryReturnDetails map(final DeliveryReturnDetail deliveryReturnDetail);

    @Mapping(target = "deviceId.value", source = "deviceId")
    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(Device device);

    ProcessType map(com.warehouse.terminal.enumeration.ProcessType processType);

    DeviceInformation map(final com.warehouse.delivery.domain.vo.DeviceInformation deviceInformation);
}
