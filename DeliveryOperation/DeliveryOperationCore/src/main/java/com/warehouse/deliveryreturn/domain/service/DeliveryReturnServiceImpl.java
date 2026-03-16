package com.warehouse.deliveryreturn.domain.service;


import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnRepository;
import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.*;
import com.warehouse.terminal.DeviceInformation;

import java.util.List;
import java.util.Set;


public class DeliveryReturnServiceImpl implements DeliveryReturnService {

    private final DeliveryReturnRepository deliveryReturnRepository;

    private final ReturnTokenServicePort returnTokenServicePort;

    private final ShipmentRepositoryServicePort shipmentRepositoryServicePort;

    private final MailServicePort mailServicePort;

    public DeliveryReturnServiceImpl(final DeliveryReturnRepository deliveryReturnRepository,
                                     final ReturnTokenServicePort returnTokenServicePort,
                                     final ShipmentRepositoryServicePort shipmentRepositoryServicePort,
                                     final MailServicePort mailServicePort) {
        this.deliveryReturnRepository = deliveryReturnRepository;
        this.returnTokenServicePort = returnTokenServicePort;
        this.shipmentRepositoryServicePort = shipmentRepositoryServicePort;
        this.mailServicePort = mailServicePort;
    }

    @Override
    public List<DeliveryReturn> deliverReturn(final Set<DeliveryReturnDetails> deliveryReturnRequests,
                                              final DeviceInformation deviceInformation) {

        return deliveryReturnRequests.stream()
                .peek(deliveryReturn -> {
                    final Shipment shipment = shipmentRepositoryServicePort.downloadShipment(deliveryReturn.getShipmentId());
                    mailServicePort.sendNotification(shipment);
                })
                .map(deliveryReturnDetails -> DeliveryReturn
                        .builder()
                        .token(deliveryReturnDetails.getReturnToken() != null
                                ? deliveryReturnDetails.getReturnToken().value()
                                : null)
                        .supplierCode(deliveryReturnDetails.getSupplierCode().value())
                        .departmentCode(deliveryReturnDetails.getDepartmentCode().getValue())
                        .shipmentId(deliveryReturnDetails.getShipmentId().getValue())
                        .deliveryStatus(deliveryReturnDetails.getDeliveryStatus().name())
                        .build())
                .toList();
    }
}
