package com.warehouse.organisationstructure.deliveryarea.domain.vo;

public record DeliveryAreaRules(boolean automaticAssignment,
                                boolean manualEditAllowed,
                                boolean recalculateOnStatusChange,
                                int maxRadiusInKilometers) {
}
