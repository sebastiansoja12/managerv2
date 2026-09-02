package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DepartmentIdApi(@NotBlank @Pattern(regexp = "[0-9]+") String value) {
}
