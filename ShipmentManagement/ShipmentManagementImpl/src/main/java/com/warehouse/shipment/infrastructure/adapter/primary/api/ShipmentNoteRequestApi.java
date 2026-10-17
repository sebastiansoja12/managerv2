package com.warehouse.shipment.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ShipmentNoteRequestApi(@NotBlank @Size(max = 4000) String body, boolean internal) {
}
