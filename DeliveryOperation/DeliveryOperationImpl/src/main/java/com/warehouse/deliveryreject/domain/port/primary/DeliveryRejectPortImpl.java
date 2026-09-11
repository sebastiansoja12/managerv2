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
import com.warehouse.deliveryreject.domain.service.DeliveryRejectConverterService;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;
import com.warehouse.terminal.DeviceInformation;

import lombok.extern.slf4j.Slf4j;
import com.warehouse.deliveryreject.domain.exception.ShipmentNotIdentifiedException;
import com.warehouse.deliveryreject.domain.port.secondary.ShipmentIdentificationServicePort;
import com.warehouse.commonassets.identificator.TrackingNumber;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    private final RejectService rejectService;

    private final RejectShipmentServicePort rejectShipmentServicePort;

    private final PersonShipmentServicePort personShipmentServicePort;

    private final DeliveryRejectConverterService deliveryRejectConverterService;
    private final ShipmentIdentificationServicePort shipmentIdentificationServicePort;

    public DeliveryRejectPortImpl(final RejectService rejectService,
                                  final RejectShipmentServicePort rejectShipmentServicePort,
                                  final PersonShipmentServicePort personShipmentServicePort,
                                  final DeliveryRejectConverterService deliveryRejectConverterService,
                                  final ShipmentIdentificationServicePort shipmentIdentificationServicePort) {
        this.rejectService = rejectService;
        this.rejectShipmentServicePort = rejectShipmentServicePort;
        this.personShipmentServicePort = personShipmentServicePort;
        this.deliveryRejectConverterService = deliveryRejectConverterService;
        this.shipmentIdentificationServicePort = shipmentIdentificationServicePort;
    }

    @Override
    public DeliveryRejectResponse deliverReject(final DeliveryRejectRequest deliveryRejectRequest) {

        final DeviceInformation device = deliveryRejectRequest.getDeviceInformation();

        deliveryRejectRequest.validateStatuses();
        identifyShipments(deliveryRejectRequest);

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

        final Map<Long, TrackingNumber> trackingNumbers = deliveryRejectRequest.getDeliveryRejectDetails().stream()
                .collect(Collectors.toMap(detail -> detail.getShipmentId().getValue(), DeliveryRejectDetails::getTrackingNumber,
                        (first, second) -> first));

        final List<DeliveryRejectResponseDetails> rejectResponseDetails = deliveryRejects.stream()
                .map(deliveryReject -> deliveryRejectConverterService.convertToDeliveryRejectResponseDetails(
                        deliveryReject,
                        shipmentRejectResponses.get(deliveryReject.getShipmentId().getValue()),
                        trackingNumbers.get(deliveryReject.getShipmentId().getValue())))
                .toList();

        return new DeliveryRejectResponse(rejectResponseDetails, deliveryRejectRequest.getDeviceInformation());
    }

    private void identifyShipments(final DeliveryRejectRequest deliveryRejectRequest) {
        for (final DeliveryRejectDetails detail : deliveryRejectRequest.getDeliveryRejectDetails()) {
            detail.setShipmentId(this.shipmentIdentificationServicePort.findShipmentId(detail.getTrackingNumber())
                    .orElseThrow(() -> new ShipmentNotIdentifiedException(detail.getTrackingNumber())));
        }
    }

    private List<ShipmentRejectResponse> notifyShipmentReject(final DeliveryRejectRequest deliveryRejectRequest) {
        final List<ShipmentRejectRequest> shipmentRejectRequests = deliveryRejectRequest.getDeliveryRejectDetails().stream()
                .map(deliveryRejectDetail -> ShipmentRejectRequest.from(deliveryRejectDetail, deliveryRejectRequest.getProcessId()))
                .toList();
        return this.rejectShipmentServicePort.notifyShipmentRejection(shipmentRejectRequests, deliveryRejectRequest.getProcessId());
    }
}
