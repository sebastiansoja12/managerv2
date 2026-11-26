package com.warehouse.analytics.domain.vo;

import java.util.List;

public record ShipmentFigures(List<RegistrationFigures> registrations, List<DeliveryFigures> deliveries) {
}
