package com.warehouse.logistics.infrastructure.adapter.secondary.mapper;

import com.google.common.collect.Lists;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.logistics.domain.vo.DepartmentCodeRequest;
import com.warehouse.routelogger.dto.*;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.model.*;
import com.warehouse.terminal.request.TerminalRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DeliveryEventMapper {

    default DeliveryRequestDto map(final DeliveryMissed deliveryMissed) {
        final DepartmentCodeDto departmentCode = map(deliveryMissed.getDepartmentCode());
        final ProcessTypeDto processType = ProcessTypeDto.MISS;
        final ShipmentIdDto shipmentId = map(deliveryMissed.getShipmentId());
        final SupplierCodeDto supplierCode = map(deliveryMissed.getSupplierCode());
        return new DeliveryRequestDto(departmentCode, processType, shipmentId, supplierCode);
    }

    SupplierCodeDto map(final SupplierCode supplierCode);

    String map(Object value);

    ShipmentIdDto map(final ShipmentId shipmentId);

    DepartmentCodeDto map(final DepartmentCode departmentCode);

    default DeviceDto map(final DeviceInformation deviceInformation) {
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().value());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(deviceInformation.getDepartmentCode().getValue());
        final UsernameDto username = new UsernameDto(deviceInformation.getUsername());
        final DeviceVersionDto deviceVersion = new DeviceVersionDto(deviceInformation.getVersion());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(deviceInformation.getDeviceType().name());
        return new DeviceDto(deviceId, departmentCode, username, deviceVersion, deviceType);
    }

    DepotCodeRequestDto mapToDepotCodeRequest(final DepartmentCodeRequest departmentCodeRequest);

    @Mapping(target = "request", source = "requestAsJson")
    default RequestDto map(final TerminalRequest terminalRequest, String requestAsJson) {
        final List<ShipmentIdDto> shipmentIds = determineShipmentIds(terminalRequest);
        return new RequestDto(requestAsJson, shipmentIds, map(terminalRequest.getProcessType()));
    }

    default List<ShipmentIdDto> determineShipmentIds(final TerminalRequest request) {
        final DeliveryRejectRequest deliveryRejectRequest = request.getDeliveryRejectRequest();
        final DeliveryReturnRequest deliveryReturnRequest = request.getDeliveryReturnRequest();
        final DeliveryMissedRequest deliveryMissedRequest = request.getDeliveryMissedRequest();
        final List<DeliveryRejectDetail> deliveryRejectDetails = deliveryRejectRequest != null ? deliveryRejectRequest.getDeliveryRejectDetails() : Lists.newArrayList();
        final List<DeliveryReturnDetail> deliveryReturnDetails = deliveryReturnRequest != null ? deliveryReturnRequest.getDeliveryReturnDetails() : Lists.newArrayList();
        final List<DeliveryMissedDetail> deliveryMissedDetails = deliveryMissedRequest != null ? deliveryMissedRequest.getDeliveryMissedDetails() : Lists.newArrayList();
        final List<ShipmentIdDto> shipmentIds = getShipmentIds(deliveryRejectDetails, deliveryReturnDetails, deliveryMissedDetails);
        return shipmentIds;
    }

	private List<ShipmentIdDto> getShipmentIds(final List<DeliveryRejectDetail> deliveryRejectDetails,
			final List<DeliveryReturnDetail> deliveryReturnDetails,
			final List<DeliveryMissedDetail> deliveryMissedDetails) {
        final List<ShipmentIdDto> shipmentIds = new ArrayList<>();
        for (final DeliveryRejectDetail deliveryRejectDetail : deliveryRejectDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryRejectDetail.getShipmentId()));
        }
        for (final DeliveryReturnDetail deliveryReturnDetail : deliveryReturnDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryReturnDetail.getShipmentId()));
        }
        for (final DeliveryMissedDetail deliveryMissedDetail : deliveryMissedDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryMissedDetail.getShipmentId()));
        }
        return shipmentIds;
    }

    ProcessTypeDto map(final ProcessType processType);
}
