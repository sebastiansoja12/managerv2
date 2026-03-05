package com.warehouse.deliveryreject.domain.port.primary;

import java.util.List;

import com.google.common.collect.Lists;
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

        //deliveryRejectRequest.rewriteSupplierCodeFromDevice();

        deliveryRejectRequest.rewriteDepartmentCodeFromDevice();

        final List<DeliveryRejectResponseDetails> rejectResponseDetails = Lists.newArrayList();

        for (final DeliveryRejectDetails deliveryRejectDetail : deliveryRejectRequest.getDeliveryRejectDetails()) {
            rejectResponseDetails.add(new DeliveryRejectResponseDetails(new RejectReasonId(1L), deliveryRejectDetail.getShipmentId(),
                    deliveryRejectDetail.getShipmentId(), deliveryRejectDetail.getSupplierCode(), deliveryRejectDetail.getDepartmentCode(),
                    deliveryRejectDetail.getRejectReason(), deliveryRejectDetail.getDeliveryStatus()));
        }

        return new DeliveryRejectResponse(rejectResponseDetails, deliveryRejectRequest.getDeviceInformation());
    }

    private ShipmentRejectResponse notifyShipmentReject(final DeliveryRejectDetails deliveryRejectDetail) {
        final ShipmentRejectRequest shipmentRejectRequest = ShipmentRejectRequest.from(deliveryRejectDetail);
        final ShipmentRejectResponse shipmentRejectResponse = this.rejectShipmentServicePort.notifyShipmentRejection(shipmentRejectRequest);
        return shipmentRejectResponse;
    }
}
