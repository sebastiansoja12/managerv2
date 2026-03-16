package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.dto.DeviceIdDto;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ProcessIdDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.ReturnTokenDto;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryReturnRequestMapper {

    default DeliveryReturnRequest map(final TerminalRequest request) {
        return new DeliveryReturnRequest(null, map(request.getProcessType()), map(request.getDevice()), Collections.emptyList());
    }

    List<DeliveryReturnDetails> map(final List<DeliveryReturnDetail> deliveryReturnRequests);

    @Mapping(target = "departmentCode.value", source = "departmentCode")
    @Mapping(target = "supplierCode.value", source = "supplierCode")
    @Mapping(target = "returnToken.value", source = "returnToken")
    DeliveryReturnDetails map(final DeliveryReturnDetail deliveryReturnDetail);

    default ShipmentId mapToShipmentId(final Long shipmentId) {
        return new ShipmentId(shipmentId);
    }

    @Mapping(target = "departmentCode.value", source = "departmentCode")
    DeviceInformation map(final Device device);

    default DeviceId map(final String deviceId) {
        return new DeviceId(deviceId);
    }

    ProcessType map(final com.warehouse.terminal.enumeration.ProcessType processType);

    DeliveryReturnRequest mapToDeliveryReturnRequest(final DeliveryReturnRequestDto deliveryReturnRequest);

    default ProcessId map(final ProcessIdDto processId) {
        return new ProcessId(processId.getProcessId());
    }

    default com.warehouse.deliveryreturn.domain.vo.ReturnToken map(final ReturnTokenDto returnToken) {
        return returnToken == null ? null : new com.warehouse.deliveryreturn.domain.vo.ReturnToken(returnToken.value());
    }

    default ShipmentId map(final ShipmentIdDto value) {
        return new ShipmentId(value.value());
    }

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "username", source = "username.value")
    DeviceInformation map(final DeviceInformationDto deviceInformation);

    default DeviceId map(DeviceIdDto deviceId) {
        return new DeviceId(deviceId.value());
    }
}
