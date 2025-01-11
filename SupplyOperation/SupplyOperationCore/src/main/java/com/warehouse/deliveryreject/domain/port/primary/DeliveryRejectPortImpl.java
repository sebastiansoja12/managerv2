package com.warehouse.deliveryreject.domain.port.primary;

import java.util.List;

import com.google.common.collect.Lists;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.port.secondary.PersonShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.port.secondary.RejectTrackerServicePort;
import com.warehouse.deliveryreject.domain.service.DeliveryRejectConverterService;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.domain.vo.*;

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

        deliveryRejectRequest.rewriteSupplierCodeFromDevice();

        deliveryRejectRequest.getDeliveryRejectDetails().forEach(deliveryRejectDetail -> {
            if (!deliveryRejectDetail.getDepartmentCode().equals(device.getDepartmentCode())) {
                throw new RuntimeException("Invalid department code");
            }
        });

        deliveryRejectRequest.rewriteDepartmentCodeFromDevice();

        final List<DeliveryRejectResponseDetails> deliveryRejectResponseDetails = Lists.newArrayList();
        for (final DeliveryRejectDetails deliveryRejectDetail : deliveryRejectRequest.getDeliveryRejectDetails()) {
            final Person recipient = this.personShipmentServicePort.getRecipient(deliveryRejectDetail.getShipmentId());
            final DeliveryReject deliveryReject = deliveryRejectConverterService.convertToDeliveryReject(deliveryRejectDetail, recipient);
            rejectService.createReject(deliveryReject);
            final ShipmentRejectResponse shipmentRejectResponse = notifyShipmentReject(deliveryRejectDetail);
            deliveryRejectResponseDetails.add(deliveryRejectConverterService.convertToDeliveryRejectResponseDetails(deliveryReject, shipmentRejectResponse));
        }

        final RejectTrackerRequest rejectTrackerRequest = RejectTrackerRequest.from(deliveryRejectRequest);
        final RejectTrackerResponse rejectTrackerResponse = this.rejectTrackerServicePort.logRejectInTracker(rejectTrackerRequest);


        return new DeliveryRejectResponse(deliveryRejectResponseDetails, deliveryRejectRequest.getDeviceInformation());
    }

    private ShipmentRejectResponse notifyShipmentReject(final DeliveryRejectDetails deliveryRejectDetail) {
        final ShipmentRejectRequest shipmentRejectRequest = ShipmentRejectRequest.from(deliveryRejectDetail);
        final ShipmentRejectResponse shipmentRejectResponse = this.rejectShipmentServicePort.notifyShipmentRejection(shipmentRejectRequest);
        return shipmentRejectResponse;
    }
}
