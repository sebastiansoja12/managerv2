package com.warehouse.shipment.application.port.primary.result;

import java.util.List;

public record ShipmentStatistics(List<ShipmentRegistrationFigures> registrations,
                                 List<ShipmentDeliveryFigures> deliveries) {
}
