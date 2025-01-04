package com.warehouse.deliveryreject.domain.port.primary;

import java.util.List;

import com.google.common.collect.Lists;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.domain.port.secondary.RejectShipmentServicePort;
import com.warehouse.deliveryreject.domain.service.DeliveryRejectConverterService;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryRejectPortImpl implements DeliveryRejectPort {

    private final RejectService rejectService;

    private final RejectShipmentServicePort rejectShipmentServicePort;

    private final DeliveryRejectConverterService deliveryRejectConverterService;

    public DeliveryRejectPortImpl(final RejectService rejectService,
                                  final RejectShipmentServicePort rejectShipmentServicePort,
                                  final DeliveryRejectConverterService deliveryRejectConverterService) {
        this.rejectService = rejectService;
        this.rejectShipmentServicePort = rejectShipmentServicePort;
        this.deliveryRejectConverterService = deliveryRejectConverterService;
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
            final Person recipient = this.rejectShipmentServicePort.getRecipient(deliveryRejectDetail.getShipmentId());
            final DeliveryReject deliveryReject = deliveryRejectConverterService.convertToDeliveryReject(deliveryRejectDetail, recipient);
            final DeliveryReject updatedDeliveryReject = rejectService.createReject(deliveryReject);
            deliveryRejectResponseDetails.add(deliveryRejectConverterService.convertToDeliveryRejectResponseDetails(updatedDeliveryReject));
        }

        return new DeliveryRejectResponse(deliveryRejectResponseDetails, deliveryRejectRequest.getDeviceInformation());
    }
}
