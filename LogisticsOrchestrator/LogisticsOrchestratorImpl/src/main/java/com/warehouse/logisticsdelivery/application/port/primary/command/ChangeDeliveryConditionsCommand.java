package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;

import java.time.LocalDateTime;

public record ChangeDeliveryConditionsCommand(DeliveryId deliveryId,
                                              DeliveryMethod method,
                                              Boolean signatureRequired,
                                              String instructions,
                                              LocalDateTime plannedAt) {
}
