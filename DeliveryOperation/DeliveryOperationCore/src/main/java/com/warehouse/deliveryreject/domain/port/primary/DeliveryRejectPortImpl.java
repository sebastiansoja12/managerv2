package com.warehouse.deliveryreject.domain.port.primary;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.port.secondary.PersonShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectTrackerServicePort;
import com.warehouse.deliveryreject.domain.service.DeliveryRejectConverterService;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;
import com.warehouse.terminal.DeviceInformation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    private final RejectService rejectService;

    private final RejectShipmentServicePort rejectShipmentServicePort;

    private final PersonShipmentServicePort personShipmentServicePort;

    private final DeliveryRejectConverterService deliveryRejectConverterService;

    private final RejectTrackerServicePort rejectTrackerServicePort;

    public DeliveryRejectPortImpl(final RejectService rejectService,
                                  final RejectShipmentServicePort rejectShipmentServicePort,
                                  final PersonShipmentServicePort personShipmentServicePort,
                                  final DeliveryRejectConverterService deliveryRejectConverterService,
                                  final RejectTrackerServicePort rejectTrackerServicePort) {
        this.rejectService = rejectService;
        this.rejectShipmentServicePort = rejectShipmentServicePort;
        this.personShipmentServicePort = personShipmentServicePort;
        this.deliveryRejectConverterService = deliveryRejectConverterService;
        this.rejectTrackerServicePort = rejectTrackerServicePort;
    }

    @Override
    public DeliveryRejectResponse deliverReject(final DeliveryRejectRequest deliveryRejectRequest) {

        final DeviceInformation device = deliveryRejectRequest.getDeviceInformation();

        deliveryRejectRequest.validateStatuses();

        deliveryRejectRequest.rewriteDepartmentCodeFromDevice();

        final List<DeliveryReject> deliveryRejects = Lists.newArrayList();

        for (final DeliveryRejectDetails deliveryRejectDetail : deliveryRejectRequest.getDeliveryRejectDetails()) {
            final DeliveryReject deliveryReject = new DeliveryReject(new RejectReasonId(null), deliveryRejectDetail.getShipmentId(),
                    deliveryRejectDetail.getDepartmentCode(), deliveryRejectDetail.getSupplierCode(),
                    device.getDeviceId(), deliveryRejectDetail.getDeliveryStatus(), deliveryRejectDetail.getRejectReason(),
                    deliveryRejectDetail.getRecipient());

            deliveryRejects.add(this.rejectService.createReject(deliveryReject));
        }

        final Map<Long, ShipmentRejectResponse> shipmentRejectResponses = notifyShipmentReject(deliveryRejectRequest)
                .stream()
                .collect(Collectors.toMap(response -> response.shipmentId().getValue(), Function.identity()));

        final List<DeliveryRejectResponseDetails> rejectResponseDetails = deliveryRejects.stream()
                .map(deliveryReject -> deliveryRejectConverterService.convertToDeliveryRejectResponseDetails(
                        deliveryReject,
                        shipmentRejectResponses.get(deliveryReject.getShipmentId().getValue())))
                .toList();

        return new DeliveryRejectResponse(rejectResponseDetails, deliveryRejectRequest.getDeviceInformation());
    }

    private List<ShipmentRejectResponse> notifyShipmentReject(final DeliveryRejectRequest deliveryRejectRequest) {
        final List<ShipmentRejectRequest> shipmentRejectRequests = deliveryRejectRequest.getDeliveryRejectDetails().stream()
                .map(deliveryRejectDetail -> ShipmentRejectRequest.from(deliveryRejectDetail, deliveryRejectRequest.getProcessId()))
                .toList();
        return this.rejectShipmentServicePort.notifyShipmentRejection(shipmentRejectRequests, deliveryRejectRequest.getProcessId());
    }
}
