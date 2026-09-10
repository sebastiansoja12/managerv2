package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;

public record CommentDeliveryStepCommand(DeliveryId deliveryId, int stepNumber, String comment) {
}
