package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.google.common.collect.Lists;
import com.warehouse.delivery.domain.vo.DepartmentCodeRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.dto.*;
import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.model.*;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryEventMapper {

    @Mapping(target = "processType", constant = "MISS")
    DeliveryRequestDto map(DeliveryMissed deliveryMissed);

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

    @Mapping(target = "processType", constant = "MISS")
    SupplierCodeRequestDto mapToSupplierCodeRequest(DeliveryMissed deliveryMissed);

    @Mapping(target = "terminalId", source = "device.deviceId")
    TerminalLogRequestDto mapToTerminalLogRequest(TerminalRequest terminalRequest);

    @Mapping(target = "version", source = "device.version")
    VersionLogRequestDto mapToVersionLogRequest(TerminalRequest terminalRequest);
}
