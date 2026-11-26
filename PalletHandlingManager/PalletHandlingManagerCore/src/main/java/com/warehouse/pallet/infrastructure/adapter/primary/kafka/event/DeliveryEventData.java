package com.warehouse.pallet.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.pallet.configuration.identificator.DepartmentId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeliveryEventData(String targetType, String targetId, DepartmentId originDepartmentId,
                                DepartmentId destinationDepartmentId) {
}
