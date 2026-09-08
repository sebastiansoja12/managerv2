package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;

public record CorrectDeliveryStepStatusCommand(DeliveryId deliveryId, int stepNumber, DeliveryStatus deliveryStatus) {
}
