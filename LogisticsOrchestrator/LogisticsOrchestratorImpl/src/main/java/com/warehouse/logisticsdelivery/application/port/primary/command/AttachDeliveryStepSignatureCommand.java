package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.SignatureId;

public record AttachDeliveryStepSignatureCommand(DeliveryId deliveryId, int stepNumber, SignatureId signatureId) {
}
