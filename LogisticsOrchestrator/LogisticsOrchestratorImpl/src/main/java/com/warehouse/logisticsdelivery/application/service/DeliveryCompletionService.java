package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SignatureRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.SignatureMethod;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.Signature;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.nio.charset.StandardCharsets;

public class DeliveryCompletionService {

    private final OperatorContextProvider operatorContextProvider;

    private final ShipmentServicePort shipmentServicePort;

    private final SignatureRepository signatureRepository;

    private final DeliveryChangeService deliveryChangeService;

    public DeliveryCompletionService(final OperatorContextProvider operatorContextProvider,
                                     final ShipmentServicePort shipmentServicePort,
                                     final SignatureRepository signatureRepository,
                                     final DeliveryChangeService deliveryChangeService) {
        this.operatorContextProvider = operatorContextProvider;
        this.shipmentServicePort = shipmentServicePort;
        this.signatureRepository = signatureRepository;
        this.deliveryChangeService = deliveryChangeService;
    }

    public DeliverySnapshot finish(final Delivery delivery, final SignatureId signatureId) {
        delivery.complete(delivery.getSupplierId(), this.operatorContextProvider.currentUserId(),
                this.operatorContextProvider.currentDepartmentId(), signatureId);
        if (delivery.getTarget().type() == DeliveryTargetType.SHIPMENT && delivery.getShipmentId() != null) {
            this.shipmentServicePort.markDelivered(delivery.getShipmentId());
        }
        return this.deliveryChangeService.save(delivery, DeliveryCompleted::new);
    }

    public SignatureId createRecipientSignature(final String recipientName, final String signature) {
        if (signature == null || signature.isBlank()) {
            throw new DeliveryModificationException("Recipient signature content is required");
        }
        final Signature recipientSignature = new Signature(recipientName, SignatureMethod.HANDWRITTEN, null,
                signature.getBytes(StandardCharsets.UTF_8));
        this.signatureRepository.save(recipientSignature);
        return recipientSignature.getSignatureId();
    }
}
