package com.warehouse.logistics.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.logistics.domain.vo.DepartmentCodeRequest;
import com.warehouse.routelogger.dto.*;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.jaxb.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collections;
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
        final DeliveryRejectRequestType deliveryRejectRequest = request.getDeliveryRejectRequest();
        final DeliveryReturnRequestType deliveryReturnRequest = request.getDeliveryReturnRequest();
        final DeliveryMissedRequestType deliveryMissedRequest = request.getDeliveryMissedRequest();
        final List<DeliveryRejectDetailType> deliveryRejectDetails = deliveryRejectRequest != null
                && deliveryRejectRequest.getDeliveryRejectDetails() != null
                ? deliveryRejectRequest.getDeliveryRejectDetails().getDeliveryRejectDetail()
                : Collections.emptyList();
        final List<DeliveryReturnDetailType> deliveryReturnDetails = deliveryReturnRequest != null
                && deliveryReturnRequest.getDeliveryReturnDetails() != null
                ? deliveryReturnRequest.getDeliveryReturnDetails().getDeliveryReturnDetail()
                : Collections.emptyList();
        final List<DeliveryMissedDetailType> deliveryMissedDetails = deliveryMissedRequest != null
                && deliveryMissedRequest.getDeliveryMissedDetails() != null
                ? deliveryMissedRequest.getDeliveryMissedDetails().getDeliveryMissedDetail()
                : Collections.emptyList();
        final List<ShipmentIdDto> shipmentIds = getShipmentIds(deliveryRejectDetails, deliveryReturnDetails, deliveryMissedDetails);
        return shipmentIds;
    }

	private List<ShipmentIdDto> getShipmentIds(final List<DeliveryRejectDetailType> deliveryRejectDetails,
			final List<DeliveryReturnDetailType> deliveryReturnDetails,
			final List<DeliveryMissedDetailType> deliveryMissedDetails) {
        final List<ShipmentIdDto> shipmentIds = new ArrayList<>();
        for (final DeliveryRejectDetailType deliveryRejectDetail : deliveryRejectDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryRejectDetail.getShipmentID()));
        }
        for (final DeliveryReturnDetailType deliveryReturnDetail : deliveryReturnDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryReturnDetail.getShipmentID()));
        }
        for (final DeliveryMissedDetailType deliveryMissedDetail : deliveryMissedDetails) {
            shipmentIds.add(new ShipmentIdDto(deliveryMissedDetail.getShipmentID()));
        }
        return shipmentIds;
    }

    ProcessTypeDto map(final ProcessTypeEnum processType);
}
